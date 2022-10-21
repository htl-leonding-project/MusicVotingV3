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
  playlistSongs: Song[] =[];

  query: string= "";
  songTitle: string ="";

  buttonDisable = false;
  buttonLikeDisable = false;

  ngOnInit(): void {

  }

  constructor(private service: SongService){
  }

  search(){
      this.service.getSearchResult(this.query).subscribe(
        {
          next: result => {
          this.songs = result;
        }}
      )

  }


  addSong(song: Song){
    this.buttonDisable = true;
    this.service.addSong(song).subscribe({
      error: ()=>{
        alert("Video zu lang oder Titel beinhaltet Zeichen die nicht verarbeitet werden können")
      }
    })

    setTimeout(()=>{
      this.buttonDisable = false;
    },environment.timeOutAtAdd);

  	console.log(this.query)
  }
}
