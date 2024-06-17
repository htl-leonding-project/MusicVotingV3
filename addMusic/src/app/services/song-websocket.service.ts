import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SongWebSocketService {
  private webSocket?: WebSocket;
  private songSubject: Subject<any>;

  constructor() {
    this.songSubject = new Subject<any>();
  }

  public connect(): void {
    this.webSocket = new WebSocket('ws://localhost:8080/songs');

    this.webSocket.onmessage = (event) => {
      const song = JSON.parse(event.data);
      this.songSubject.next(song);
    };

    this.webSocket.onopen = () => {
      console.log('WebSocket connection opened');
    };

    this.webSocket.onclose = () => {
      console.log('WebSocket connection closed');
    };

    this.webSocket.onerror = (error) => {
      console.error('WebSocket error', error);
    };
  }

  public getSongUpdates(): Observable<any> {
    return this.songSubject.asObservable();
  }
}
