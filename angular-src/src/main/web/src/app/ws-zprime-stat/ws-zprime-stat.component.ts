import {Component, OnInit} from '@angular/core';
import {WebsoketService} from "../service/websoket.service";

@Component({
  selector: 'app-ws-zprime-stat',
  templateUrl: './ws-zprime-stat.component.html',
  styleUrls: ['./ws-zprime-stat.component.css']
})
export class WsZprimeStatComponent implements OnInit {

  public color = 'primary';
  public mode = 'determinate';

  constructor(public websoketService: WebsoketService) { }

  ngOnInit() {
  }

}
