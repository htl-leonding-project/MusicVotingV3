import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { SongService } from '../services/song.service';
import { Router } from '@angular/router';
import {md5} from '../../md5';
import {GlobalService} from "../services/global.service";
import {environment} from "../../environments/environment";

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
    console.log("Okay clicked")
    this.songService.checkPassword(md5(this.password)).subscribe({
      next: ()=> {
        console.log("Passwort stimmt")
        this.errorMessage = ""
        this.globalService.password = md5(this.password)
        this.dialogRef.close();
        console.log("closing dialog")
      },
      error: ()=> {
        this.errorMessage = "Passwort ist falsch"
        console.log("wrong pw")
      }
    })
  }
}
