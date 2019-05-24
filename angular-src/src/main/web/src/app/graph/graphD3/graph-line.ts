import * as d3 from 'd3';
import {MainGraph} from './main-graph';
import {LineConfig} from './line-config';

export class GraphLine {
    
    private line;
    private lineView;

    protected data: any[] = [];

    constructor(lineConfig: LineConfig) {
        this.line = d3.svg.line()
            .x(function(d) { return MainGraph.x(d[0]); })
            .y(function(d) { return MainGraph.y(d[1]); });

        this.line.interpolate(lineConfig.getInterpilate());

        // TODO-ilia remove in css class
        this.lineView = MainGraph.svg.append("path").style("stroke-dasharray", ("7, 4, 4, 4"));
        if (lineConfig.getData()) {
            this.data = lineConfig.getData();
            // add data
            this.lineView.datum(this.data);
            // start render
            this.lineView.attr("d", this.line);

        }
        // css class necessary
        this.lineView.attr("class", lineConfig.getCssClass());
    }

    public setData(data) {
        for(let i = 0; i < data.length; i++) {
            // value is 0 the graphic will broken because 0 less then 10^-4 and it is broking
            if (data[i][1] === 0) {
                data[i][1] = 0.0001;
            }
        }
        this.data = data;
        this.lineView.datum(this.data);
        this.lineView.attr("d", this.line);
    }

    public removeData() {
        this.lineView[0][0].remove();
    }

    public setPoint(point) {
        let insertIndex = -1;
        for(let i = 0; i < this.data.length; i++) {
            // if the mass of point alredy exists then update value
            if (this.data[i][0] === point[0]) {
                this.data[i][1] = point[1];
                break;
            }
            // if mass of point less current mass then insert point
            if (point[0] < this.data[i][0]) {
                insertIndex = i;
                break;
            }
        }
        if (insertIndex > -1) {
            this.data.splice(insertIndex, 0, point);
        } else {
            // the point mass more then mass of another points in line
            this.data.push(point);
        }
        this.setData(this.data);
    }
}
