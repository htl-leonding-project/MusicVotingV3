import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DialogBodyComponent } from '../dialog-body/dialog-body.component';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-qr-code',
  templateUrl: './qr-code.component.html',
  styleUrls: ['./qr-code.component.css']
})
export class QrCodeComponent implements OnInit {

  qrCodeUrl = 'https://www.polizei.gv.at/'

  constructor(
    private matDialog: MatDialog,

  ) { }

  ngOnInit(): void {
    this.openPasswordDialog()
  }

  openPasswordDialog() {
    const dialogConfig = new MatDialogConfig()
    dialogConfig.disableClose = true
    let dialogref=this.matDialog.open(DialogBodyComponent, dialogConfig)
    dialogref.afterClosed().subscribe(result => {
      this.qrCodeUrl = "http://music.htl-leonding.ac.at/"+environment.home
    });

  }

}
