import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatTabsModule} from '@angular/material/tabs';
import { MatDialogModule } from '@angular/material/dialog';
import { DialogBodyComponent } from './dialog-body/dialog-body.component';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { HomeComponent } from './home/home.component';
import {SafePipe, ShowMusicComponent} from './show-music/show-music.component';
import { QrCodeComponent } from './qr-code/qr-code.component';
import { QRCodeModule } from 'angularx-qrcode';
import {YouTubePlayerModule} from "@angular/youtube-player";
import { AppRoutingModule } from "./app-routing.module";
import {RouterModule, Routes} from "@angular/router";
import {environment} from "../environments/environment";

const routes: Routes = [
  { path: 'adminPage', component: AdminPageComponent },
  { path: environment.home,   component: HomeComponent},
  { path: 'show',   component: ShowMusicComponent},
  { path: 'qr',   component: QrCodeComponent},
  { path: '',   redirectTo: '/'+environment.home, pathMatch: 'full' },
];


@NgModule({
    declarations: [
        AppComponent, DialogBodyComponent, AdminPageComponent, HomeComponent, ShowMusicComponent, QrCodeComponent, SafePipe
    ],
    imports: [
        BrowserModule,
        FormsModule,
        AppRoutingModule,
        HttpClientModule,
        BrowserAnimationsModule,
        MatButtonModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        MatTabsModule,
        MatDialogModule,
        QRCodeModule,
        YouTubePlayerModule,
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule { }
