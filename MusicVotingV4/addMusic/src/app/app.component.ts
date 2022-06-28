import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Song } from './modules/song.module';
import { SongService } from './services/song.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'addMusic';

  songs: Song[] =[];

  artist: string= "";
  songTitle: string ="";

  buttonDisable = false;

  ngOnInit(): void {

  }

  constructor(private service: SongService){
  }

  search(){
    if(this.songTitle != ""){
      this.service.getSearchResultWithTitle(this.artist,  this.songTitle).subscribe(
        {
          next: result => {
          this.songs = result;
        }}
      )
    }
    else{
      this.service.getSearchResultNoTitle(this.artist).subscribe(
        {
          next: result => {
          this.songs = result;
        }}
      )
    }
  }


  addSong(song: Song){
    this.buttonDisable = true;
    this.service.addSong(this.artist,  song.songName).subscribe()

    setTimeout(()=>{
      this.buttonDisable = false;
    },environment.timeOutAtAdd);

  	console.log(this.artist)
  }
}
