import {LedgedConfig} from "./legend-config";

export class LineConfig {

    private textConfig: LedgedConfig;
    private cssClass;
    private interpolate: string = 'monotone';
    private data;
    private legendConfig: LedgedConfig;

    constructor(cssClass, legendConfig: LedgedConfig) {
        this.cssClass = cssClass;
        this.legendConfig = legendConfig;
    }

    public setTextConfig(textConfig: LedgedConfig) {
        this.textConfig = textConfig;
    }

    public getTextConfig() {
        return this.textConfig;
    }

    public getCssClass() {
        return this.cssClass;
    }

    public getLedgedConfig(): LedgedConfig {
        return this.legendConfig;
    }

    public setInterpolate(interpolateMethod) {
        this.interpolate = interpolateMethod;
    }

    public getInterpilate() {
        return this.interpolate;
    }

    public setData(data) {
        this.data = data;
    }

    public getData() {
        return this.data;
    }
}
