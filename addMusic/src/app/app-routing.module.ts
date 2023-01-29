import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { ShowMusicComponent } from './show-music/show-music.component';
import { QrCodeComponent } from './qr-code/qr-code.component';
import { environment } from 'src/environments/environment';

const routes: Routes = [
  { path: 'adminPage', component: AdminPageComponent },
  { path: environment.home,   component: HomeComponent},
  { path: 'show',   component: ShowMusicComponent},
  { path: 'qr',   component: QrCodeComponent},
  { path: '',   redirectTo: '/'+environment.home, pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
