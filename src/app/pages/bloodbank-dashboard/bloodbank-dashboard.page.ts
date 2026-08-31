import { Component } from '@angular/core';
import {
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonIcon
} from '@ionic/angular/standalone';

@Component({
  selector: 'app-bloodbank-dashboard',
  templateUrl: './bloodbank-dashboard.page.html',
  styleUrls: ['./bloodbank-dashboard.page.scss'],
  standalone: true,
  imports: [
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonIcon
  ]
})
export class BloodbankDashboardPage {

}