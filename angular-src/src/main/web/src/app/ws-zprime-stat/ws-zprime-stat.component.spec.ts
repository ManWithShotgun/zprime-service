import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {WsZprimeStatComponent} from './ws-zprime-stat.component';

describe('WsZprimeStatComponent', () => {
  let component: WsZprimeStatComponent;
  let fixture: ComponentFixture<WsZprimeStatComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WsZprimeStatComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WsZprimeStatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
