import {Component, OnInit, Pipe, PipeTransform, Sanitizer, SecurityContext, ViewChild} from '@angular/core';
import {Song} from '../modules/song.module';
import {SongService} from '../services/song.service';
import {interval, Observable} from 'rxjs';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {DialogBodyComponent} from '../dialog-body/dialog-body.component';
import {DomSanitizer} from "@angular/platform-browser";
import {YouTubePlayer} from "@angular/youtube-player";

@Component({
  selector: 'app-show-music',
  templateUrl: './show-music.component.html',
  styleUrls: ['./show-music.component.css']
})

export class ShowMusicComponent implements OnInit {
  @ViewChild('player', {static: true}) player: YT.Player  | undefined;
  isPlaying = false;
  isPausedClicked = true;
  btnDisabled = true
  actSong: Song = {
    videoUrl: '',
    duration: 0,
    songName: '',
    thumbnail: '',
    voteCount: 1,
    id: '',
    songId: ''
  };
  songs: Song[] = [];

  constructor(
    private songService: SongService,
    private matDialog: MatDialog,
  ) {
  }


  ngOnInit(): void {
    this.openPasswordDialog()

    const tag = document.createElement("script")
    tag.src = "https://www.youtube.com/iframe_api"
    document.body.appendChild(tag)
  }

  openPasswordDialog() {
    const dialogConfig = new MatDialogConfig()
    dialogConfig.disableClose = true
    let dialogref = this.matDialog.open(DialogBodyComponent, dialogConfig)
    dialogref.afterClosed().subscribe(result => {
      this.btnDisabled = false
      interval(1000).subscribe((x) => {
        this.songService.getPlaylist().subscribe({
          next: (res) => {
            this.songs = res
          },
        })
      });
    })
  }

  playNextSong() {
    if (this.songs.length != 0) {
      let songToDelete = this.songs.shift();
      console.log("Deleting song: " + songToDelete?.songName);
      this.songService.deleteSong(songToDelete?.songId!)
      this.actSong = this.songs[0];
      console.log("New song: " + this.actSong.songName)
    }

  }

  onStateChanged(event: any) {
    console.log("Event: " + event.data)
    if (event.data == YT.PlayerState.ENDED) {
      console.log("Song Ended, playing next")
      this.playNextSong()
    }
  }

  startPlaying() {
    if (!this.isPlaying) {
      this.playNextSong()
      this.isPlaying = true;
    }
  }

  testNewVid() {
    // this.actSong = new class implements Song {
    //   duration: number = 44;
    //   id: string = "234234";
    //   songId: string = "2";
    //   songName: string = "Crab";
    //   thumbnail: string = "";
    //   videoUrl: string = "https://www.youtube.com/watch?v=LDU_Txk06tM";
    //   voteCount: number = 1;
    // };



    this.player?.pauseVideo();
  }
}


@Pipe({
  name: 'safe'
})
export class SafePipe
  implements PipeTransform {

  constructor(private sanitizer: DomSanitizer) {
  }

  transform(url: any) {
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

}
