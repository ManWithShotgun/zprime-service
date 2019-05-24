import * as d3 from 'd3';
import {GraphArea} from './graph-area';
import {GraphText} from './graph-text';
import {SSMContainer} from './impl/ssm-container';
import {FocusModule} from './impl/focus-module';
import {WebsoketService} from "../../service/websoket.service";
import {LedgedConfig} from './legend-config';
import {LinePoints} from './line-points';
import {GraphSataticData} from './graph-static-data';

export class MainGraph {

    public static svg;
    public static x;
    public static y;
    public static logPen;
    private domainX = [1, 5000];
    private domainY = [Math.pow(10, -4), Math.pow(10, 1)];
    private container: SSMContainer;
    private wsReceivePointSubscription;
    private wsReceiveLineSubscription;

    public static renderSvg(conf): any {
        return MainGraph.svg.append("svg:image")
            .attr('x', conf.x)
            .attr('y', conf.y)
            .attr('width', conf.width)
            .attr('height', conf.height)
            .attr("xlink:href", conf.href);
    }

    constructor(private selector: string, private webSocketService: WebsoketService) {
        this.webSocketService.getConnection().then(client => {
            // receive one point
            this.wsReceivePointSubscription = client.subscribe("/user/data/reply", this.receiveData.bind(this));
            // receive list of points
            this.wsReceiveLineSubscription = client.subscribe("/user/data-all/reply", this.receiveWholeLine.bind(this));
            return client;
        });
        // create promise chain and end set .then(init)
    }

    public onGraphDestroy() {
        this.webSocketService.getConnection().then(client => {
            this.wsReceiveLineSubscription.unsubscribe();
            this.wsReceivePointSubscription.unsubscribe();
            return client;
        });
    }

    // Request to backend
    public requestWholeLine(ksi, events, cycles) {
        this.container.addLine(ksi, events, cycles);
        this.webSocketService.getWholeLineData(ksi, events, cycles);
    }

    private receiveData(response) {
        if (response.body) {
            let resObject = JSON.parse(response.body);
            if (resObject.statusCodeValue === 200) {
                let res = resObject.body;
                if (!res.result) {
                    console.log('Result is null for ksi: ' + res.ksi + " mass: " + res.mass);
                    return;
                }
                let line = LinePoints.pointToFloat([res.mass, res.result]);
                if (line[1] < this.domainY[0]) {
                    line[1] = this.domainY[0];
                }
                this.webSocketService.pointReceived();
                this.container.setPoint(line, res.ksi, res.events, res.cycles);
            } else {
                this.webSocketService.pointReceivedWithError();
            }
          }
    }

    private receiveWholeLine(response) {
        if (response.body) {
            let resObject = JSON.parse(response.body);
            if (resObject.statusCodeValue === 200) {
                // before render the data should be converted and sorted
                let res = resObject.body;
                let line = new LinePoints(res.result);
                this.container.setData(line, res.ksi, res.events, res.cycles);
                this.requestMissingPointsOfLine(res.ksi, res.events, res.cycles, line);
            }
        }   
    }

    private requestMissingPointsOfLine(ksi, events, cycles, line: LinePoints): void {
        // send requests based on config:
        // 1. step between points
        // 2. start mass (0)
        // 3. end mass (5000)
        let startMass = 0;
        let endMass = 5000;
        let step = 100;
        let calculatedMass = line.getData().map(point => point[0]);
        for(startMass = 100; startMass <= endMass; startMass += step) {
            if (!calculatedMass.includes(startMass)) {
                this.webSocketService.getData(ksi, events, cycles, startMass);
            }
        }
    }

    public init() {
        let margin = {top: 20, right: 30, bottom: 60, left: 60},
            width = 760 - margin.left - margin.right,
            height = 500 - margin.top - margin.bottom;
        MainGraph.x = d3.scale.linear()
            .domain(this.domainX)
            .range([0, width]);
        
        MainGraph.y = d3.scale.log()
            .domain(this.domainY)
            .range([height, 0]);

        MainGraph.logPen = d3.svg.line()
            .interpolate("monotone")
            .x(function(d) { return MainGraph.x(d[0]); })
            .y(function(d) { return MainGraph.y(d[1]); });
        
        let xAxis = d3.svg.axis()
            .scale(MainGraph.x)
            .orient("bottom");
        
        let yAxis = d3.svg.axis()
            .scale(MainGraph.y)
            .orient("left")
            .ticks(0, "e")
            .tickFormat(function (d) {
                let log = Math.log(d) / Math.LN10;
                return Math.abs(Math.round(log) - log) < 1e-6 ? 10 : '';
            });
        let yAxisSub = d3.svg.axis()
            .scale(MainGraph.y)
            .orient('left')
            .ticks(0, "e")
            .tickFormat(function (d) {
                let log = Math.log(d) / Math.LN10;
                return Math.abs(Math.round(log) - log) < 1e-6 ? Math.round(log) : '';
            });

        MainGraph.svg = d3.select(this.selector).append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        MainGraph.svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis);
        
        MainGraph.svg.append("g")
            .attr("class", "y axis")
            .call(yAxis);
        MainGraph.svg.append('g')
            .attr("class", "y axis-y-sub")
            .attr("transform", "translate(5, -8)")
            .style("font-size", "8px")
            .call(yAxisSub);

        // render main legend
        MainGraph.renderSvg({
            x: 300,
            y: -150,
            width: 400,
            height: 400,
            href: 'https://i.imgur.com/GBsjqgs.png'
        });

        // y sign 
        MainGraph.renderSvg({
            x: -160,
            y: 100,
            width: 250,
            height: 250,
            href: 'https://i.imgur.com/vuLA2Os.png'
        });

        // x sign
        MainGraph.renderSvg({
            x: 300,
            y: 403,
            width: 100,
            height: 100,
            href: 'https://i.imgur.com/uFOY07U.png'
        });

        this.container = new SSMContainer();

        this.renderExpectedArea(GraphSataticData.getExpectedData());
        this.renderRef(GraphSataticData.getReferenceModelData());
        this.renderObserved(GraphSataticData.getObservedData());
        new FocusModule(width, height, this.domainX, this.domainY);
    }

    public getLinesKsi(): Array<string> {
        // TODO: get events + cycles
        return Array.from(this.container.getLines().keys());
    }

    public createLineInfo(ksi, events, cycles): string {
        return ksi + '(Events: ' + events + ' Cycles: ' + cycles + ')';
    }

    public removeLine(lineKey): void {
        this.container.removeData(lineKey);
    }

    public renderExpectedArea(data) {
        new GraphArea({
            data,
            class: 'yellow-area',
            offset: 25
        });
        new GraphArea({
            data,
            class: 'green-area',
            offset: 15
        });
        MainGraph.svg.append("path")
		    .datum(data)
            .attr("class", "line-expected")
            .attr("d", MainGraph.logPen);
    }

    private renderObserved(data) {
        // rend line
        MainGraph.svg.append("path")
		    .datum(data)
            .attr("class", "line-observed")
            .attr("d", MainGraph.logPen);

        MainGraph.svg.selectAll(".circle")
            .data(data)
            .enter().append("circle")
            .attr("cx", MainGraph.logPen.x())
            .attr("cy", MainGraph.logPen.y())
            .style("fill", "black")
            .attr("r", 3);
    }

    public renderRef(data) {
        let textConfig = new LedgedConfig('Reference model', 70, 40);
        textConfig.setCssClass('ref-text');
        new GraphText(textConfig);

        MainGraph.svg.append("path")
		    .datum(data)
            .attr("class", "line-ref")
            .attr("d", MainGraph.logPen);
        // elements with class .rectangles isn't exist
        MainGraph.svg.selectAll(".rectangles")
            .data(data)
            .enter().append("rect")
            .attr("x", MainGraph.logPen.x())
            .attr("y", function(d) { return MainGraph.y(d[1]) - 2; })
            .style("fill", "blue")
            .attr("width", 5)
            .attr("height", 7);
    }
}
