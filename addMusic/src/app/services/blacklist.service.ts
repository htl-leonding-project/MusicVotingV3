import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { BlacklistItem } from '../modules/blacklist-item.model';
import { GlobalService } from './global.service';

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

  constructor(private http: HttpClient, private globalService: GlobalService) { }

  getBlacklist(){
    return this.http.get<BlacklistItem[]>(this.url);
  }

  addToBlacklist(phrase: string){
    return this.http.post(this.url+"/"+phrase+"/"+this.globalService.password, {});
  }

  deleteFromBlacklist(blacklistItemId: number){
    return this.http.delete(this.url+blacklistItemId+"/"+this.globalService.password);
  }
}
