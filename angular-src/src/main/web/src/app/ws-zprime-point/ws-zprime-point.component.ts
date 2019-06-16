import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {DoubleValidator} from '../ws-control/double.validator';

@Component({
  selector: 'app-ws-zprime-point',
  templateUrl: './ws-zprime-point.component.html',
  styleUrls: ['./ws-zprime-point.component.css']
})
export class WsZprimePointComponent implements OnInit {

  requestData = this.fb.group({
    ksi: [null, [Validators.required, DoubleValidator.isDouble]],
    mass: [null, [Validators.required, DoubleValidator.isDouble]],
    events: [null, [Validators.required]],
    cycles: [null, [Validators.required]]
  });

  constructor(private fb: FormBuilder) { }

  ngOnInit() {
  }

}
