import { Component, OnInit } from '@angular/core';
import { Song } from '../modules/song.module';
import { SongService } from '../services/song.service';
import { interval, Observable } from 'rxjs';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DialogBodyComponent } from '../dialog-body/dialog-body.component';


@Component({
  selector: 'app-show-music',
  templateUrl: './show-music.component.html',
  styleUrls: ['./show-music.component.css']
})
export class ShowMusicComponent implements OnInit {

  title = 'showMusic';
  isPlaying = false;
  window?: Window|null;
  isPausedClicked = true;
  btnDisabled = true

  actSong: Song = {
    videoUrl: '',
    duration: 0,
    songName: '',
    thumbnail: '',
    voteCount:1,
    id:'',
    songId:''
    };

  constructor(
    private songService: SongService,
    private matDialog: MatDialog,
      ) {}

  songs: Song[] = [];
  actIndex: number = -1;

  ngOnInit(): void {
    this.openPasswordDialog()

  }

  openPasswordDialog() {
    const dialogConfig = new MatDialogConfig()
    dialogConfig.disableClose = true
    let dialogref=this.matDialog.open(DialogBodyComponent, dialogConfig)
    dialogref.afterClosed().subscribe(result => {
      this.btnDisabled = false
      interval(1000).subscribe((x) => {
        this.songService.getPlaylist().subscribe({
          next: (res) => {
            this.songs = res
          },
        })

        if(this.songs.length > 0
          && this.isPlaying == false
          && this.isPausedClicked == false){
          this.playSong()
        }
      })
    });

  }

  goToLink(song: Song) {
    window.open(song.videoUrl, '_blank');
  }


  playNextSong() {
    if (this.isPlaying == true) {

      this.songService.getNextSong().subscribe({
        next: (song) => {
          if (song != null) {
            this.actSong = song;
            this.window = window.open(
              song.videoUrl,
              '',
              'toolbar=no,scrollbars=no,resizable=no,width=500,height=300,menubar=no,titlebar=no'
            );


            setTimeout(() => {
              console.log('song vorbei');

              if (this.window != null) {
                console.log('fenster schließen');
                this.window.close();
              }

              this.playNextSong();
            }, song.duration);
          } else {
            console.log('Playlist ende');
            song = {
              videoUrl: '',
              duration: 0,
              songName: '',
              thumbnail: '',
              voteCount:1,
              id:"",
              songId:''
            };
            this.isPlaying = false;
          }
        },
      });
    }
  }

  playSong() {
    this.isPlaying = !this.isPlaying;
    this.isPausedClicked = !this.isPausedClicked;
    if (this.isPlaying == true) {
      this.playNextSong();
    }
    else{
      if(this.window != null){
        this.window.close()
        window.location.reload()
      }
    }
  }
}
