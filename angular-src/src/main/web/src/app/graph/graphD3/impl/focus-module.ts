import * as d3 from 'd3';
import {MainGraph} from '../main-graph';
import {SSMContainer} from './ssm-container';

export class FocusModule {
    private static context;
    private checkLine;

    constructor(width, height, domainX, domainY) {
        FocusModule.context = this;
        var focus = MainGraph.svg.append("g")
        .attr("class", "focus")
        .style("display", "none");

        MainGraph.svg.append("rect")
            .attr("class", "overlay")
            .attr("width", width)
            .attr("height", height)
            .on("mouseover", function() { focus.style("display", null); })
            .on("mouseout", function() { focus.style("display", "none"); })
            .on("mousemove", this.mousemove);
        this.initCheckLine(domainX, domainY);
    }

    private initCheckLine(domainX, domainY) {
        this.checkLine = MainGraph.svg.append("path")
            .attr("class", "ssm-line")
            .attr("d", function(d){
                return MainGraph.logPen([
                    [domainX[0], domainY[1]], 
                    [domainX[0], domainY[0]]
                ]);
        });
    }

    public mousemove() {
        let x0 = MainGraph.x.invert(d3.mouse(this)[0]);
        FocusModule.context.mouseMoveInvoke(x0);
    }

    public mouseMoveInvoke(x0) {
        this.checkLine.attr('transform', 'translate(' + MainGraph.x(x0) + ')');
        // invoke SSMContainer change X
        SSMContainer.updateX(x0);
    }
}