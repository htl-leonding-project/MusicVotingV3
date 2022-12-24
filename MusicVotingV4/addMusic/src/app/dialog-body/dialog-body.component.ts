import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { SongService } from '../services/song.service';

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
    private songService: SongService
    )
  {}

  ngOnInit(): void {
  }

  close() {
    this.dialogRef.close();
  }

  ok() {
    this.songService.checkPassword(this.password).subscribe({
      next: ()=> {
        console.log("Passwort stimmt")
        this.errorMessage = ""
      },
      error: ()=> {
        this.errorMessage = "Passwort ist falsch"
      }
    })
  }
}
