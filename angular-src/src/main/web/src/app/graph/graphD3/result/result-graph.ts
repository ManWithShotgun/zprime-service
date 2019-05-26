import * as d3 from 'd3';

export class ResultGraph {

    public static svg;
    public static x;
    public static y;
    public static logPen;
    private domainX = [500, 5000];
    private domainY = [Math.pow(10, -4), Math.pow(10, 1)];

    public static renderSvg(conf): any {
        return ResultGraph.svg.append("svg:image")
            .attr('x', conf.x)
            .attr('y', conf.y)
            .attr('width', conf.width)
            .attr('height', conf.height)
            .attr("xlink:href", conf.href);
    }

    constructor(private selector: string) {

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
            x: 300,
            y: -150,
            width: 400,
            height: 400,
            href: 'https://i.imgur.com/GBsjqgs.png'
        });

        // y sign 
        ResultGraph.renderSvg({
            x: -160,
            y: 100,
            width: 250,
            height: 250,
            href: 'https://i.imgur.com/vuLA2Os.png'
        });

        // x sign
        ResultGraph.renderSvg({
            x: 300,
            y: 403,
            width: 100,
            height: 100,
            href: 'https://i.imgur.com/uFOY07U.png'
        });
    }
}
