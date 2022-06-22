import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { ISong } from './isong';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'ShowMusic';

  currentProgress$ = new BehaviorSubject(0);
  currentTime$ = new Subject();

  @ViewChild('player', { static: true }) player: ElementRef = new ElementRef(
    ''
  );

  songs: ISong[] = [];

  audio = new Audio();
  isPlaying = false;
  isPaused = false;
  activeSong: ISong = this.songs[0];
  durationTime: string = '';

  ngOnInit() {
    this.songs = this.getListOfSongs();
    this.player.nativeElement.src = this.songs[0];
    this.player.nativeElement.load();
    this.activeSong = this.songs[0];
    this.isPlaying = true;
    this.player.nativeElement.src = this.activeSong.path;
    this.player.nativeElement.play();

  }

  playSong(song: ISong): void {
    if(this.isPaused == true){
      this.player.nativeElement.play();
      this.isPaused = false;
      this.isPlaying = true;
    }
    else{
      this.durationTime = '';
      this.player.nativeElement.load();

      this.audio.pause();
      this.player.nativeElement.src = song.path;

    this.player.nativeElement.play();
    this.activeSong = song;
    this.isPlaying = true;
    }

  }

  onTimeUpdate() {
    // Set song duration time
    if (!this.durationTime) {
      this.setSongDuration();
    }

    // Emit converted audio currenttime in user friendly ex. 01:15
    const currentMinutes = this.generateMinutes(
      this.player.nativeElement.currentTime
    );
    const currentSeconds = this.generateSeconds(
      this.player.nativeElement.currentTime
    );
    this.currentTime$.next(
      this.generateTimeToDisplay(currentMinutes, currentSeconds)
    );

    // Emit amount of song played percents
    const percents = this.generatePercentage(
      this.player.nativeElement.currentTime,
      this.player.nativeElement.duration
    );
    if (!isNaN(percents)) {
      this.currentProgress$.next(percents);
    }
  }

  // Play song that comes after active song
  playNextSong(): void {
    this.isPaused = false;
    const nextSongIndex = this.songs.findIndex(
      (song) => song.id === this.activeSong.id + 1
    );

    if (nextSongIndex === -1) {
      this.playSong(this.songs[0]);
    } else {
      this.playSong(this.songs[nextSongIndex]);
    }
  }

  // Play song that comes before active song
  playPreviousSong(): void {
    this.isPaused = false;
    const prevSongIndex = this.songs.findIndex(
      (song) => song.id === this.activeSong.id - 1
    );
    if (prevSongIndex === -1) {
      this.playSong(this.songs[this.songs.length - 1]);
    } else {
      this.playSong(this.songs[prevSongIndex]);
    }
  }

  // Calculate song duration and set it to user friendly format, ex. 01:15
  setSongDuration(): void {
    const durationInMinutes = this.generateMinutes(
      this.player.nativeElement.duration
    );
    const durationInSeconds = this.generateSeconds(
      this.player.nativeElement.duration
    );

    if (!isNaN(this.player.nativeElement.duration)) {
      this.durationTime = this.generateTimeToDisplay(
        durationInMinutes,
        durationInSeconds
      );
    }
  }
  // Generate minutes from audio time
  generateMinutes(currentTime: number): number {
    return Math.floor(currentTime / 60);
  }

  // Generate seconds from audio time
  generateSeconds(currentTime: number): string {
    const secsFormula = Math.floor(currentTime % 60);
    return secsFormula < 10
      ? '0' + String(secsFormula)
      : secsFormula.toString();
  }

  generateTimeToDisplay(
    currentMinutes: number,
    currentSeconds: string
  ): string {
    return `${currentMinutes}:${currentSeconds}`;
  }

  // Generate percentage of current song
  generatePercentage(currentTime: number, duration: number): number {
    return Math.round((currentTime / duration) * 100);
  }

  onPause(): void {
    this.isPlaying = false;
    this.isPaused = true;
    //this.currentProgress$.next(0);
    //this.currentTime$.next('0:00');
    //this.durationTime = "";
  }

  getListOfSongs(): ISong[] {
    return [
      // {
      //   id: 1,
      //   title: 'Rick Astley - Never Gonna Give You Up (Official Music Video).mp3',
      //   path: './assets/Rick Astley - Never Gonna Give You Up (Official Music Video).mp3'
      // },
      {
        id: 2,
        title: 'One Direction - What Makes You Beautiful (Lyrics).mp4',
        path: './assets/One Direction - What Makes You Beautiful (Lyrics).mp4',
      },
      // {
      //   id: 3,
      //   title: 'Survivor - Eye Of The Tiger (Official HD Video).mp3',
      //   path: './assets/Survivor - Eye Of The Tiger (Official HD Video).mp3'
      // }
    ];
  }
}
