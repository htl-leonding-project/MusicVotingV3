import { TestBed } from '@angular/core/testing';

import { SongWebsocketService } from './song-websocket.service';

describe('SongWebsocketService', () => {
  let service: SongWebsocketService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SongWebsocketService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
