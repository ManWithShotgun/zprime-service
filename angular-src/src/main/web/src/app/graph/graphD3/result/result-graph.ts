import * as d3 from 'd3';
import {GraphSataticData} from '../graph-static-data';
import {HttpClient} from '@angular/common/http';
import {LinePoints} from '../line-points';

export class ResultGraph {

    public static svg;
    public static x;
    public static y;
    public static logPen;
    private domainX = [500, 5000];
    private domainY = [Math.pow(10, -5), Math.pow(10, -2)];

    public static renderSvg(conf): any {
        return ResultGraph.svg.append("svg:image")
            .attr('x', conf.x)
            .attr('y', conf.y)
            .attr('width', conf.width)
            .attr('height', conf.height)
            .attr("xlink:href", conf.href);
    }

    constructor(private selector: string, private http: HttpClient) {

    }

    public init() {
        let margin = {top: 20, right: 30, bottom: 60, left: 60},
            width = 760 - margin.left - margin.right,
            height = 500 - margin.top - margin.bottom;
        ResultGraph.x = d3.scale.linear()
            .domain(this.domainX)
            .range([0, width]);
        
        ResultGraph.y = d3.scale.log()
            .domain(this.domainY)
            .range([height, 0]);

        ResultGraph.logPen = d3.svg.line()
            .interpolate("linear")
            .x(function(d) { return ResultGraph.x(d[0]); })
            .y(function(d) { return ResultGraph.y(d[1]); });
        
        let xAxis = d3.svg.axis()
            .scale(ResultGraph.x)
            .orient("bottom");
        
        let yAxis = d3.svg.axis()
            .scale(ResultGraph.y)
            .orient("left")
            .ticks(0, "e")
            .tickFormat(function (d) {
                let log = Math.log(d) / Math.LN10;
                return Math.abs(Math.round(log) - log) < 1e-6 ? 10 : '';
            });
        let yAxisSub = d3.svg.axis()
            .scale(ResultGraph.y)
            .orient('left')
            .ticks(0, "e")
            .tickFormat(function (d) {
                let log = Math.log(d) / Math.LN10;
                return Math.abs(Math.round(log) - log) < 1e-6 ? Math.round(log) : '';
            });

        ResultGraph.svg = d3.select(this.selector).append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        ResultGraph.svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis);
        
        ResultGraph.svg.append("g")
            .attr("class", "y axis")
            .call(yAxis);
        ResultGraph.svg.append('g')
            .attr("class", "y axis-y-sub")
            .attr("transform", "translate(5, -8)")
            .style("font-size", "8px")
            .call(yAxisSub);

        // render main legend
        ResultGraph.renderSvg({
            x: 500,
            y: -20,
            width: 150,
            height: 150,
            href: 'https://i.imgur.com/7I7mzBI.png'
        });

        // y sign 
        ResultGraph.renderSvg({
            x: -40,
            y: 180,
            width: 20,
            height: 20,
            href: 'https://i.imgur.com/xZsV7cb.png'
        });

        // x sign
        ResultGraph.renderSvg({
            x: 300,
            y: 403,
            width: 100,
            height: 100,
            href: 'https://i.imgur.com/uFOY07U.png'
        });

        // EW constraints
        let constraintsLine = ResultGraph.svg.append("path").style("stroke-dasharray", ("7, 4, 4, 4"));
        constraintsLine.datum([[500, 0.00257], [5000, 0.00257]]);
        constraintsLine.attr("class", "ew-constraints-line");
        constraintsLine.attr("d", ResultGraph.logPen);
        ResultGraph.svg.append("text")
            .attr("class", 'constraints-text')
            .attr("x", 280)         
            .attr("y", 75)         
            .text('EW constraints');

        // LHC DY
        let lineView = ResultGraph.svg.append("path").style("stroke-dasharray", ("7, 4, 4, 4"));
        lineView.datum([[4250, 0.00001], [4250, 0.00257]]);
        lineView.attr("class", "lhc-dy-line");
        lineView.attr("d", ResultGraph.logPen);
        ResultGraph.svg.append("text")
            .attr("class", 'lhc-dy-text')
            .attr("x", 480)         
            .attr("y", 400)         
            .text('LHC DY');

        // FB 36
        this.renderFb(250, 150, '36.1 fb', 50);
        // FB 1000
        this.renderFb(250, 200, '100 fb', 50);
        this.renderFb(250, 260, '1000 fb', 54);
        

        this.renderObserved(GraphSataticData.getResultObservedData());

        

        this.http.get('http://localhost:8080/collisions').subscribe(this.receiveCollitions.bind(this));

    }

    private receiveCollitions(data) {
        let points = new LinePoints(data);
        let dataLines = points.getData().filter(line => {return line[0] > 500});
        this.renderLine(dataLines);
        let thousandFbLines = dataLines.map(line => {return [line[0], line[1] * 0.19]});
        this.renderLine(thousandFbLines);
        let hangredFbLines = dataLines.map(line => {return [line[0], line[1] * 0.66]});
        this.renderLine(hangredFbLines);
    }

    private renderLine(data) {
        let line = ResultGraph.svg.append("path").style("stroke-dasharray", ("7, 4, 4, 4"));
        line.datum(data);
        line.attr("class", "lhc-dy-line");
        line.attr("d", ResultGraph.logPen);
    }

    private renderObserved(data) {
        // rend line
        ResultGraph.svg.append("path")
		    .datum(data)
            .attr("class", "line-result-observed")
            .attr("d", ResultGraph.logPen);

        ResultGraph.svg.selectAll(".circle")
            .data(data)
            .enter().append("circle")
            .attr("cx", ResultGraph.logPen.x())
            .attr("cy", ResultGraph.logPen.y())
            .style("fill", "black")
            .attr("r", 3);
    }

    private renderFb(x, y, text, plusX) {
        ResultGraph.svg.append("text")
            .attr("class", 'fb-text')
            .attr("x", x)         
            .attr("y", y)         
            .text(text);
        ResultGraph.svg.append("text")
            .attr("class", 'fb-exponent-text')
            .attr("x", x + plusX)         
            .attr("y", y - 6)         
            .text('-1');
    }
}
