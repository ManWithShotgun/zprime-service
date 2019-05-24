import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {WsControlComponent} from './ws-control.component';

describe('WsControlComponent', () => {
  let component: WsControlComponent;
  let fixture: ComponentFixture<WsControlComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WsControlComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WsControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
