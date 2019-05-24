import * as d3 from 'd3';
import {MainGraph} from './main-graph';

export class GraphArea {
    
    protected area;
    protected areaView;

    constructor(lineConfig: any) {
        this.area = d3.svg.area()
            .interpolate("basis")
            .x(function(d) { return MainGraph.x(d[0]); })
            .y0(function(d) { return MainGraph.y(d[1]) + lineConfig.offset;})
            .y1(function(d) { return MainGraph.y(d[1]) - lineConfig.offset;});

        this.areaView = MainGraph.svg.append("path");
        this.areaView.datum(lineConfig.data);
        this.areaView.attr("class", "area " + lineConfig.class)
            .attr("d", this.area);
    }
}