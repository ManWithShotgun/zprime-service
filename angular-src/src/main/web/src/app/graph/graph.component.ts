import {Component, ViewEncapsulation} from '@angular/core';
import {MainGraphService} from "./main-graph.service";


@Component({
    selector: 'graph-comp',
    templateUrl: './graph.component.html',
    encapsulation: ViewEncapsulation.None,
    styleUrls: ['./graph.component.css']
})
export class Graph {

    constructor(private mainGraphService: MainGraphService) {}

    ngOnInit() {
        this.mainGraphService.renderMain();
    }

    ngOnDestroy() {
        this.mainGraphService.ngOnDestroy();
    }
}

