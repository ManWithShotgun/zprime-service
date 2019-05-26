import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ResultGraph} from '../graph/graphD3/result/result-graph';

@Component({
  selector: 'app-result-graph',
  templateUrl: './result-graph.component.html',
  encapsulation: ViewEncapsulation.None,
  styleUrls: ['./result-graph.component.css']
})
export class ResultGraphComponent implements OnInit {

  private MAIN_GRAPH_SELECTOR: string = 'div#svg';

  constructor(private http: HttpClient) { }

  ngOnInit() {
    new ResultGraph(this.MAIN_GRAPH_SELECTOR, this.http).init();
  }

}
