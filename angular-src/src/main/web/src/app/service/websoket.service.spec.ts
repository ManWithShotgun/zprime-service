import {inject, TestBed} from '@angular/core/testing';

import {WebsoketService} from './websoket.service';

describe('WebsoketService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [WebsoketService]
    });
  });

  it('should be created', inject([WebsoketService], (service: WebsoketService) => {
    expect(service).toBeTruthy();
  }));
});
