import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {WsZprimePointComponent} from './ws-zprime-point.component';

describe('WsZprimePointComponent', () => {
  let component: WsZprimePointComponent;
  let fixture: ComponentFixture<WsZprimePointComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WsZprimePointComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WsZprimePointComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
