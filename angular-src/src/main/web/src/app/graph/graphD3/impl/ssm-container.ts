import {SSMLine} from './ssm-line';
import {LineConfig} from '../line-config';
import {LedgedConfig} from '../legend-config';
import {LinePoints} from '../line-points';

export class SSMContainer {
    private static context;
    private static LINE_CLASS = 'ssm-line';
    private static TEXT_CLASS = 'ssm-text';
    private static LEGEND_CLASS = 'ssm-legend-text';
    private static LEGEND_TEXT_PREFIX = 'ssm ';

    private static LEGEND_START_X = 470;
    private static LEGEND_START_Y = 150;
    private static LEGEND_STEP_Y = 20;

    private lendgesForLines: Map<string, any> = new Map();
    private lines: Map<string, SSMLine> = new Map();

    constructor() {
        this.lendgesForLines.set('0.01', {x: 180, y: 70});
        this.lendgesForLines.set('0.0002', {x: 270, y: 140});
        SSMContainer.context = this;
    }

    public getLines(): Map<string, SSMLine> {
        return this.lines;
    }

    public addLine(ksi, events, cycles) {
        let lineNumber: number = this.lines.size;

        // set legend config
        let legendConfig = new LedgedConfig(SSMContainer.LEGEND_TEXT_PREFIX + ksi, SSMContainer.LEGEND_START_X, 
            SSMContainer.LEGEND_START_Y + SSMContainer.LEGEND_STEP_Y * lineNumber);
        legendConfig.setCssClass(SSMContainer.LEGEND_CLASS);
        let lineConfig = new LineConfig(SSMContainer.LINE_CLASS, legendConfig);
        // set text for line if exists
        let textPosition = this.lendgesForLines.get(ksi);
        if (textPosition) {
            let textConfig = new LedgedConfig(ksi, textPosition.x, textPosition.y);
            textConfig.setCssClass(SSMContainer.TEXT_CLASS);
            if (lineNumber === 0) {
                textConfig.mustRenderZ();
            }
            lineConfig.setTextConfig(textConfig);
        }
        let line = new SSMLine(lineConfig);
        this.lines.set(ksi + '_' + events + '_' + cycles, line);
    }

    public setData(line: LinePoints, ksi, events, cycles) {
        this.lines.get(ksi + '_' + events + '_' + cycles).setData(line.getData());
    }

    public setPoint(point, ksi, events, cycles) {
        this.lines.get(ksi + '_' + events + '_' + cycles).setPoint(point);
    }

    public removeData(lineKey) {
        this.lines.get(lineKey).delete();
        this.lines.delete(lineKey);
        let i = 0;
        this.lines.forEach(line => {
            line.updateLegendPosition(SSMContainer.LEGEND_START_X, SSMContainer.LEGEND_START_Y + SSMContainer.LEGEND_STEP_Y * i);
            i++;
        });
    }

    public static updateX(x0) {
        SSMContainer.context.updateX(x0);
    }

    private updateX(x0) {
        this.lines.forEach((line) => {
            line.updateX(x0);
        });
    }
    
}
