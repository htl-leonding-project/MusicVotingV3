import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Song } from '../modules/song.module';

@Injectable({
  providedIn: 'root',
})
export class SongService {
  constructor(private http: HttpClient) {}

  getNextSong() {
    return this.http.get<Song>(environment.host+"/getNextSong");
  }
  
  getPlaylist() {
    return this.http.get<Song[]>(environment.host);
  }
}
