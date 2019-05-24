import {MainGraph} from '../main-graph';
import {GraphText} from '../graph-text';
import {LedgedConfig} from '../legend-config';


// example:
// {
//     text: '=0.01',
//     class: 'ssm-text',
//     x: 170,
//     y: 140,
//     urlZ: true
// }
export class SSMText extends GraphText {
    private static urlZ: string = 'http://svgshare.com/i/3p9.svg';
    private static urlE: string = 'http://svgshare.com/i/3sF.svg';
    constructor(textConfig: LedgedConfig) {
        super(textConfig);
        this.renderE();
        if (textConfig.canRenderZ()) {
            this.renderZ();
        }
    }

    private renderZ() {
        MainGraph.renderSvg({
            x: this.textSign.x - 70,
            y: this.textSign.y - 30,
            width: 45,
            height: 45,
            href: SSMText.urlZ
        });
    }

    private renderE() {
        MainGraph.renderSvg({
            x: this.textSign.x - 18,
            y: this.textSign.y - 16,
            width: 22,
            height: 22,
            href: SSMText.urlE
        });
    }
}
