import {MainGraph} from './main-graph';
import {LedgedConfig} from './legend-config';

// TODO: simplify this - don't save textSign
export class GraphText {
    protected textSign: {text: string, x, y};
    private textView;
    constructor(textConfig: LedgedConfig) {
        this.textSign = {
            text: textConfig.getText(),
            x: textConfig.getX(),
            y: textConfig.getY()
        };
        this.textView = MainGraph.svg.append("text")
            .attr("class", textConfig.getCssClass())
            .attr("x", this.textSign.x)         
            .attr("y", this.textSign.y)         
            .text(this.textSign.text);
    }

    public delete(): void {
        this.textView[0][0].remove();
    }

    public updateText(text) {
        this.textSign.text = text;
        this.textView.text(this.textSign.text);
    }

    public updateTextPosition(x, y) {
        this.textSign.x = x;
        this.textSign.y = y;
        this.textView.attr("x", this.textSign.x);   
        this.textView.attr("y", this.textSign.y);     
    }
}
