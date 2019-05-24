export class LedgedConfig {

    private text;
    private x: number;
    private y: number;
    private cssClass;
    private renderZ: boolean = false;


    constructor(text, x, y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public getText() {
        return this.text;
    }

    public setCssClass(cssClass) {
        this.cssClass = cssClass;
    }

    public getCssClass() {
        return this.cssClass;
    }

    public getX() {
        return this.x;
    }

    public getY() {
        return this.y;
    }

    public mustRenderZ() {
        this.renderZ = true;
    }

    public canRenderZ() {
        return this.renderZ;
    }
}