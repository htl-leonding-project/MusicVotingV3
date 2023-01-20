import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { interval, Observable } from 'rxjs';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Song } from '../modules/song.module';
import { SongService } from '../services/song.service';
import { DialogBodyComponent } from '../dialog-body/dialog-body.component';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  title = 'addMusic';

  songs: Song[] =[];
  playlistSongs: Song[] =[];

  query: string= "";
  songTitle: string ="";

  buttonDisable = false;
  buttonLikeDisable = false;


  constructor(
    private service: SongService,
    private router: Router
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
      error: (err: HttpErrorResponse)=>{
        alert(err.error)
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

  adminBtnClicked() {
    this.router.navigate(["/adminPage"])
  }
}
