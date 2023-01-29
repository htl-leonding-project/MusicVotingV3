import { Component, OnInit } from '@angular/core';
import { Song } from '../modules/song.module';
import { SongService } from '../services/song.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DialogBodyComponent } from '../dialog-body/dialog-body.component';
import { BlacklistItem } from '../modules/blacklist-item.model';
import { BlacklistService } from '../services/blacklist.service';
import { interval } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css'],
})
export class AdminPageComponent implements OnInit {

  songs: Song[] = []
  blacklist: BlacklistItem[]= []
  blacklistphrase: string = ""
  buttonDisable = true

  constructor(
    private matDialog: MatDialog,
    private songService: SongService,
    private blacklistService: BlacklistService,
    private router:Router
    ) {
  }

  ngOnInit(): void {
    this.openPasswordDialog()
  }

  openPasswordDialog() {
    const dialogConfig = new MatDialogConfig()
    dialogConfig.disableClose = true
    let dialogref=this.matDialog.open(DialogBodyComponent, dialogConfig)
    dialogref.afterClosed().subscribe(result => {
      interval(1000).subscribe((x) => {
        this.songService.getPlaylist().subscribe({
          next: (res) => {
            this.songs = res
          },
        })
      })
      this.refreshBlacklist()
      this.buttonDisable = false
    });

  }


  onBlackList(phrase: string) {
    phrase.replace("/", "")
    phrase.replace("\\", "")

    this.blacklistService.putOnBlacklist(phrase).subscribe({
      next: ()=> {
        this.refreshBlacklist()
      }
    })
  }

  deleteSong(songId: string) {
    console.log(songId)
    this.songService.deleteSong(songId).subscribe()
  }

  deleteBlacklistItem(blacklistId: number) {
    console.log(blacklistId)
    this.blacklistService.delteFromBlacklist(blacklistId).subscribe({
      next: () => {
        this.refreshBlacklist()
      }
    })
  }

  refreshBlacklist(){
    this.blacklistService.getBlacklist().subscribe({
      next: res=>{
        this.blacklist = res
      }
    })
  }

  backToHome(){
    this.router.navigate(['/'+environment.home]);
  }

  navigateToShow() {
    this.router.navigate(['/show']);
    }
}
