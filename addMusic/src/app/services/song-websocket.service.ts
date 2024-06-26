import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class SongWebSocketService {
  private webSocket?: WebSocket;
  private songSubject: Subject<any>;
  private url = environment.host + "songs";

  constructor() {
    this.songSubject = new Subject<any>();
  }

  public connect(): void {
    this.webSocket = new WebSocket('ws://' + this.url.replace('http://', ''));

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
