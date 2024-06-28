import {Component, OnInit} from '@angular/core';
import {environment} from 'src/environments/environment';
import {Subscription} from 'rxjs';
import {Song} from '../modules/song.module';
import {SongService} from '../services/song.service';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';
import {SongWebSocketService} from "../services/song-websocket.service";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  title = 'addMusic';

  songs: Song[] = [];
  playlistSongs: Song[] = [];
  likedSongs: Song[] = [];
  addedSongs: Song[] = [];

  query: string = "";
  songTitle: string = "";
  buttonDisable = false;
  buttonLikeDisable = false;
  private songSubscription: Subscription | undefined;


  constructor(
    private service: SongService,
    private router: Router,
    private songWebSocketService: SongWebSocketService,
  ) {
    this.loadLikedSongsFromCookies();
    this.loadAddedSongsFromCookies();
  }


  ngOnInit(): void {
    this.songWebSocketService.connect();
    this.songSubscription = this.songWebSocketService.getSongUpdates().subscribe((song: any) => {
      this.playlistSongs = song;
    });
  }

  search() {
    this.service.getSearchResult(this.query).subscribe(
      {
        next: result => {
          this.songs = result;
        }
      }
    )
  }


  addSong(song: Song) {
    this.buttonDisable = true;
    this.addedSongs.push(song);
    localStorage.setItem('addedSongs', JSON.stringify(this.addedSongs));
    this.service.addSong(song).subscribe({
      error: (err: HttpErrorResponse) => {
        this.buttonDisable = false
        alert(err.error)
      }
    })
    this.buttonDisable = false
  }

  likeSong(song: Song) {
    this.addSong(song)
    this.likedSongs.push(song)
    localStorage.setItem('likedSongs', JSON.stringify(this.likedSongs));
  }

  adminBtnClicked() {
    this.router.navigate(["/adminPage"])
  }

  hasAlreadyBeenVoted(song: Song): boolean {
    return this.likedSongs.find(s => s.id === song.id) !== undefined;
  }

  hasAlreadyBeenAdded(song: Song): boolean {
    let isInAddedSongs = this.addedSongs.find(s => s.videoUrl === song.videoUrl) !== undefined;

    if (isInAddedSongs) {
      let isInCurrentPlaylist = this.playlistSongs.find(s => s.videoUrl === song.videoUrl) !== undefined;
      if (!isInCurrentPlaylist) {
        this.addedSongs.slice(this.addedSongs.indexOf(song),1)
        localStorage.setItem('addedSongs', JSON.stringify(this.addedSongs));
        return false;
      }
    }
    return isInAddedSongs
  }

  private loadLikedSongsFromCookies() {
    const likedSongs = localStorage.getItem('likedSongs');
    if (likedSongs) {
      this.likedSongs = JSON.parse(likedSongs);
    }
  }

  private loadAddedSongsFromCookies() {
    const addedSongs = localStorage.getItem('addedSongs');
    if (addedSongs) {
      this.addedSongs = JSON.parse(addedSongs);
    }
  }
}
