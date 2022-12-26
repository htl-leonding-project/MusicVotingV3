import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { BlacklistItem } from '../modules/blacklist-item.model';

@Injectable({
  providedIn: 'root'
})
export class BlacklistService {

  url: string = environment.host + "blacklist"

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  getBlacklist(){
    return this.http.get<BlacklistItem[]>(this.url+"/getBlacklist");
  }

  putOnBlacklist(phrase: string){
    return this.http.post(this.url+"/putOnBlackList/"+phrase, {});
  }

  delteFromBlacklist(blacklistItemId: number){
    return this.http.delete(this.url+"/deleteFromBlacklist/"+blacklistItemId);
  }
}
