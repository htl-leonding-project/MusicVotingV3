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
  likedSongs: Song[]=  [];

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
    // this.loadLikedSongsFromCookies();
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
    this.service.addSong(song).subscribe({
      error: (err: HttpErrorResponse) => {
        this.buttonDisable = false
        alert(err.error)
      }
    })
    this.buttonDisable = false

    console.log(this.query)
  }

  likeSong(song: Song) {
    this.addSong(song)
    this.likedSongs.push(song)
    // this.saveLikedSongsToCookies();
  }


  // private loadLikedSongsFromCookies() {
  //   const cookie = this.cookieService.get('likedSongs');
  //   if (cookie) {
  //     this.likedSongs = JSON.parse(cookie);
  //   }
  // }

  // private saveLikedSongsToCookies() {
  //   this.cookieService.put('likedSongs', JSON.stringify(this.likedSongs), { expires: 365 });
  // }


  adminBtnClicked() {
    this.router.navigate(["/adminPage"])
  }

  hasAlreadyBeenVoted(song: Song) : boolean{
    return this.likedSongs.find(s=> s.id === song.id) !== undefined;
  }
}
