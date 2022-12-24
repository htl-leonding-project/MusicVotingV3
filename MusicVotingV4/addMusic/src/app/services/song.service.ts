import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Song } from '../modules/song.module';

@Injectable({
  providedIn: 'root'
})
export class SongService {

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  getPlaylist(){
    return this.http.get<Song[]>(environment.host);
  }

  getSearchResult(artistName: string){
    return this.http.get<Song[]>(environment.host+"/search/"+artistName);
  }

  addSong(newSong: Song){
    return this.http.post<Song>(environment.host+"/addSong", newSong, this.httpOptions);
  }

  checkPassword(password: string){
    return this.http.get<boolean>(environment.host+"/checkPassword/"+password);
  }
}
