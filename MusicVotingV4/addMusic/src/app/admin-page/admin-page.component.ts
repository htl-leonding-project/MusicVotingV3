import { Component, OnInit } from '@angular/core';
import { Song } from '../modules/song.module';
import { SongService } from '../services/song.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DialogBodyComponent } from '../dialog-body/dialog-body.component';
import { BlacklistItem } from '../modules/blacklist-item.model';
import { BlacklistService } from '../services/blacklist.service';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css'],
})
export class AdminPageComponent implements OnInit {
  songs: Song[] = []
  blacklist: BlacklistItem[]= []
  blacklistphrase: string = ""

  constructor(
    private matDialog: MatDialog,
    private songService: SongService,
    private blacklistService: BlacklistService
    ) {
  }

  ngOnInit(): void {
    this.openPasswordDialog()
    this.songService.getPlaylist().subscribe({
      next: (res) => {
        this.songs = res
      },
    })

    this.blacklistService.getBlacklist().subscribe({
      next: res=>{
        this.blacklist = res
      }
    })
  }

  openPasswordDialog() {
    const dialogConfig = new MatDialogConfig()
    dialogConfig.disableClose = true
    this.matDialog.open(DialogBodyComponent, dialogConfig)
  }

  onBlackList() {
    this.blacklistService.putOnBlacklist(this.blacklistphrase).subscribe()
  }

  deleteSong(songId: string) {
    this.songService.deleteSong(songId).subscribe()
  }

  deleteBlacklistItem(blacklistId: string) {
    this.blacklistService.delteFromBlacklist(blacklistId).subscribe()
  }
}
