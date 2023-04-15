import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { SongService } from '../services/song.service';
import { Router } from '@angular/router';
import {md5} from '../../md5';
import { environment } from 'src/environments/environment';
import { GlobalService } from '../services/global.service';

@Component({
  selector: 'app-dialog-body',
  templateUrl: './dialog-body.component.html',
  styleUrls: ['./dialog-body.component.css']
})
export class DialogBodyComponent implements OnInit {
  errorMessage: string = ""
  password: string = ""

  constructor(
    public dialogRef: MatDialogRef<DialogBodyComponent>,
    private songService: SongService,
    private router: Router,
    private globalService: GlobalService
    )
  {}

  ngOnInit(): void {
  }

  close() {
    this.dialogRef.close();
    this.router.navigate(['/'+environment.home]);
  }


  ok() {
    this.songService.checkPassword(md5(this.password)).subscribe({
      next: ()=> {
        console.log("Passwort stimmt")
        this.errorMessage = ""
        this.globalService.password = md5(this.password)
        this.dialogRef.close();

      },
      error: ()=> {
        this.errorMessage = "Passwort ist falsch"
      }
    })
  }
}
