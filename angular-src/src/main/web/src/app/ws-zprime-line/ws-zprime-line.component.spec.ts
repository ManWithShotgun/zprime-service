import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {WsZprimeLineComponent} from './ws-zprime-line.component';

describe('WsZprimeLineComponent', () => {
  let component: WsZprimeLineComponent;
  let fixture: ComponentFixture<WsZprimeLineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WsZprimeLineComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WsZprimeLineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
