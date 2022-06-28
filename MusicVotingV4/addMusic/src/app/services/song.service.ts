import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Song } from '../modules/song.module';

@Injectable({
  providedIn: 'root'
})
export class SongService {

  constructor(private http: HttpClient) { }

  getPlaylist(){
    return this.http.get<Song[]>(environment.host);
  }

  getSearchResultWithTitle(artistName: string, songTitle: string){
    return this.http.get<Song[]>(environment.host+"/search/"+artistName+"/"+songTitle);
  }

  getSearchResultNoTitle(artistName: string){
    return this.http.get<Song[]>(environment.host+"/search/"+artistName);
  }

  addSong(artistName: string, songTitle: string){
    return this.http.get(environment.host+"/addSong/"+artistName+"/"+songTitle);
  }
}
