import * as d3 from 'd3';
import {MainGraph} from '../main-graph';
import {GraphLine} from '../graph-line';
import {GraphText} from '../graph-text';
import {SSMText} from './ssm-text';
import {LineConfig} from '../line-config';

export class SSMLine extends GraphLine {
    private bisectDate = d3.bisector(function(d) { return d[0]; }).left;
    private formatValueY = d3.format(",.4f");
    private formatValueX = d3.format(",.0f");
    private legendView: GraphText;
    private legendText: string;
    private focusGroup;

    constructor(lineConfig: LineConfig) {
        super(lineConfig);
        // set text
        if(lineConfig.getTextConfig()) {
            new SSMText(lineConfig.getTextConfig());
        }
        // set dinamic legend for line
        this.legendText = lineConfig.getLedgedConfig().getText();
        this.legendView = new GraphText(lineConfig.getLedgedConfig());
        // set focus group
        this.focusGroup = MainGraph.svg.append("g")
            .attr("class", "focus")
            .style("display", "none");
      
        this.focusGroup.append("circle")
            .attr("r", 4.5);
        
        this.focusGroup.append("text")
            .attr("x", 9)
            .attr("dy", ".35em");
        // setTimeout(() => {
        //     this.data[3] = [this.data[3][0], 8];
        //     this.setData(this.data);
        // }, 5000);

        // ?? invoke request for each x point async ??
        //
    }

    public delete(): void {
        this.removeData();
        this.focusGroup[0][0].remove();
        this.legendView.delete();
    }

    public updateX(x): void {
        let i = this.bisectDate(this.data, x, 1);
        if (i < this.data.length) {
            let d0 = this.data[i - 1],
                d1 = this.data[i], //excaption when check line sooo right side
                d = x - d0[0] > d1[0] - x ? d1 : d0;
            this.updateText(d);
            // update focus: circle and text circle
            this.updateFocus(d);
        }
    }

    private updateText(d): void {
        // update text legend
        this.legendView.updateText(this.legendText + ' ' + this.formatValueY(d[1]));
    }

    public updateLegendPosition(x, y) {
        this.legendView.updateTextPosition(x, y);
    }

    private updateFocus(d) {
        this.focusGroup.attr("transform", "translate(" + MainGraph.x(d[0]) + "," + MainGraph.y(d[1]) + ")");
        this.focusGroup.style("display", null);
        this.focusGroup.select("text").text(this.formatValueX(d[0]));
    }
}
