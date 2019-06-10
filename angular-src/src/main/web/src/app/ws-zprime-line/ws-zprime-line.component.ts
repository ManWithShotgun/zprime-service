import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {MainGraphService} from '../graph/main-graph.service';
import {DoubleValidator} from '../ws-control/double.validator';
import {AuthServiceService} from '../auth-service.service';

@Component({
  selector: 'app-ws-zprime-line',
  templateUrl: './ws-zprime-line.component.html',
  styleUrls: ['./ws-zprime-line.component.css']
})
export class WsZprimeLineComponent implements OnInit {

  requestData = this.fb.group({
    ksi: [null, [Validators.required, DoubleValidator.isDouble]],
    events: [null, [Validators.required]],
    cycles: [null, [Validators.required]],
  });

  constructor(private fb: FormBuilder, public mainGraphService: MainGraphService, private authService: AuthServiceService) { 
    authService.userIsAdmin();
  }

  ngOnInit() {
    
  }


  public requestLine() {
    this.mainGraphService.requestLine(
      this.requestData.controls.ksi.value,
      this.requestData.controls.events.value,
      this.requestData.controls.cycles.value,
    );
  }

  public removeLine(lineKey) {
    this.mainGraphService.removeLine(lineKey);
  }

  public getLinesInfo() {
    return this.mainGraphService.getLinesInfo()
    .map(lineKey => {
      let info = lineKey.split('_');
      let label = info[0] + ' (Кол-во событий: ' + info[1] + ' Кол-во циклов: ' + info[2] + ')';
      return {label, key: lineKey};
      // return label;
    });
  }

}
