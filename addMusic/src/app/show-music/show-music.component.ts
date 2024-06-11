import {Component, OnInit, Pipe, PipeTransform, Sanitizer, SecurityContext, ViewChild} from '@angular/core';
import {Song} from '../modules/song.module';
import {SongService} from '../services/song.service';
import {delay, interval, Observable} from 'rxjs';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {DialogBodyComponent} from '../dialog-body/dialog-body.component';
import {DomSanitizer} from "@angular/platform-browser";
import {YouTubePlayer} from "@angular/youtube-player";
import {NgZone} from "@angular/core";

@Component({
  selector: 'app-show-music',
  templateUrl: './show-music.component.html',
  styleUrls: ['./show-music.component.css']
})

export class ShowMusicComponent implements OnInit {
  isPlaying = false;
  isPausedClicked = true;
  btnDisabled = true
  player: YT.Player | null = null;
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
            if(this.songs.length != 0) {
              this.actSong = this.songs[0]
            }
          },
        })
      });
    })
  }

  playNextSong() {
    console.log("Playing next song")
    if (this.songs.length != 0) {
      console.log(this.songs)
      let songToDelete = this.songs.shift();
      console.log("Deleting song: " + songToDelete?.songName);

      this.songService.deleteSong(songToDelete?.songId!)
      this.actSong = this.songs[0];

      console.log("New song: " + this.actSong.songName + this.actSong.videoUrl)

      this.player?.loadVideoById(this.getSongIdFromSong(this.actSong))
    }
  }
  startPlaying() {
    if (!this.isPlaying) {
      this.isPlaying = true;
      this.player = new YT.Player('player', {
        height: '360',
        width: '640',
        videoId: this.getSongIdFromSong(this.actSong),
        events: {
          "onReady": this.onPlayerReady,
          'onStateChange': this.onStateChanged
        }
      });
    }
  }

  getSongIdFromSong(song: Song) {
    return song.videoUrl.substring(song.videoUrl.indexOf('?v=') + 3, song.videoUrl.length)
  }

  onStateChanged = (event: any) => {
    if (event.data == YT.PlayerState.ENDED) {
      this.playNextSong();
    }
  };

  onPlayerReady = (event: any) => {
    event.target.playVideo();
  };

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
