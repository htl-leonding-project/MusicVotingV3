import {Component, OnDestroy, OnInit, Pipe, PipeTransform, Sanitizer, SecurityContext, ViewChild} from '@angular/core';
import {Song} from '../modules/song.module';
import {SongService} from '../services/song.service';
import {delay, interval, Observable, Subscription} from 'rxjs';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {DialogBodyComponent} from '../dialog-body/dialog-body.component';
import {DomSanitizer} from "@angular/platform-browser";
import {YouTubePlayer} from "@angular/youtube-player";
import {NgZone} from "@angular/core";
import {SongWebSocketService} from "../services/song-websocket.service";

@Component({
  selector: 'app-show-music',
  templateUrl: './show-music.component.html',
  styleUrls: ['./show-music.component.css']
})

export class ShowMusicComponent implements OnInit, OnDestroy {
  isPlaying = false;
  isPausedClicked = true;
  btnDisabled = false
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
  private songSubscription: Subscription | undefined;

  constructor(
    private songService: SongService,
    private matDialog: MatDialog,
    private songWebSocketService: SongWebSocketService,
  ) {
  }


  ngOnInit(): void {
    this.openPasswordDialog()
    const tag = document.createElement("script")
    tag.src = "https://www.youtube.com/iframe_api"
    document.body.appendChild(tag)

    this.songWebSocketService.connect();
    this.songSubscription = this.songWebSocketService.getSongUpdates().subscribe((song: any) => {
      this.songs = song;
      console.log(song)
    });
  }

  ngOnDestroy(): void {
    if (this.songSubscription) {
      this.songSubscription.unsubscribe();
    }
  }

  openPasswordDialog() {
    const dialogConfig = new MatDialogConfig()
    dialogConfig.disableClose = true
    let dialogref = this.matDialog.open(DialogBodyComponent, dialogConfig)
    dialogref.close()
  }

  playNextSong() {
    console.log("Playing next song")
    if (this.songs.length != 0) {

      this.deleteSong();

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

  deleteSong() {
    let songToDelete = this.songs.shift();
    console.log("Deleting song: " + songToDelete?.songName);

    this.songService.deleteSong(songToDelete?.songId!)
    this.actSong = this.songs[0];
    console.log(this.songs)

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
