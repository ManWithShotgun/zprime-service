export class LinePoints {

    private data = [];

    constructor(objectOfPoints) {
        let keys = Object.keys(objectOfPoints);
        let values = Object.values(objectOfPoints);
        let responseData = [];
        for(let i = 0; i < keys.length; i++) {
            responseData.push([keys[i], values[i]]);
        }
        this.data = responseData.map(this.toFloat).sort(this.sortMatrix);
    }

    public getData() {
        return this.data;
    }

    //TODO: logic for point class
    public static pointToFloat(point) {
        return [parseFloat(point[0]), parseFloat(point[1])];
    }

    private toFloat(point) {
        return LinePoints.pointToFloat(point);
    }

    private sortMatrix(a, b): number {
        if (a[0] === b[0]) {
            return 0;
        }
        else {
            return (a[0] < b[0]) ? -1 : 1;
        }
    }

}