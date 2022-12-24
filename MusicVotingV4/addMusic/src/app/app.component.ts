import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Song } from './modules/song.module';
import { SongService } from './services/song.service';
import { interval, Observable } from 'rxjs';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DialogBodyComponent } from './dialog-body/dialog-body.component';


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


  constructor(
    private service: SongService,
    private matDialog: MatDialog
    ){
  }


  ngOnInit(): void {
    interval(1000).subscribe((x) => {
      this.service.getPlaylist().subscribe({
        next: (result) => {
          this.playlistSongs = result;
        },
      });
    });
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
      this.buttonDisable = false
    },environment.timeOutAtAdd)

  	console.log(this.query)
  }

  likeSong(song: Song){
    this.buttonLikeDisable = true
    this.addSong(song)

    setTimeout(()=>{
      this.buttonLikeDisable = false
    },environment.timeOutAtAdd)
  }

  openPasswordDialog() {
    const dialogConfig = new MatDialogConfig();
    this.matDialog.open(DialogBodyComponent, dialogConfig);
  }
}
