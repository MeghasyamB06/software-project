import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
  IonContent,
  IonIcon
} from '@ionic/angular/standalone';

import { addIcons } from 'ionicons';

import {
  waterOutline,
  notificationsOutline,
  personCircleOutline,
  locationOutline,
  alertCircleOutline,
  businessOutline,
  calendarOutline,
  checkmarkCircleOutline,
  chevronForwardOutline,
  documentTextOutline,
  settingsOutline,
  personOutline,
  homeOutline,
  medicalOutline,
  heartOutline,
  closeOutline,
  timeOutline
} from 'ionicons/icons';


// =====================================================
// EMERGENCY REQUEST
// =====================================================

interface EmergencyRequest {

  level: string;

  status: string;

  bloodGroup: string;

  unitsRequired: number;

  hospitalName: string;

  distance: string;

  requiredBy: string;

}


// =====================================================
// DONATION
// =====================================================

interface Donation {

  date: string;

  hospital: string;

  bloodGroup: string;

  units: number;

  status: string;

}


// =====================================================
// NOTIFICATION
// =====================================================

interface Notification {

  message: string;

  time: string;

  unread: boolean;

}


// =====================================================
// COMPONENT
// =====================================================

@Component({

  selector: 'app-donor-dashboard',

  templateUrl: './donor-dashboard.page.html',

  styleUrls: ['./donor-dashboard.page.scss'],

  standalone: true,

  imports: [

    CommonModule,

    IonContent,

    IonIcon

  ]

})


export class DonorDashboardPage implements OnInit {


  // =====================================================
  // LOGGED-IN DONOR INFORMATION
  // =====================================================

  donorName = 'Donor';

  donorEmail = '';

  bloodGroup = 'O+';

  city = 'Hyderabad';

  isAvailable = true;


  // =====================================================
  // DONATION STATISTICS
  // =====================================================

  totalDonations = 3;

  livesHelped = 9;

  lastDonation = '12 Jun 2026';

  nextEligibleDate = '12 Sep 2026';


  // =====================================================
  // EMERGENCY REQUESTS
  // =====================================================

  emergencyRequests: EmergencyRequest[] = [

    {

      level: 'CRITICAL',

      status: 'Open',

      bloodGroup: 'O+',

      unitsRequired: 2,

      hospitalName: 'City Care Hospital',

      distance: '4.2 km',

      requiredBy: 'Within 2 hours'

    },

    {

      level: 'URGENT',

      status: 'Open',

      bloodGroup: 'O+',

      unitsRequired: 1,

      hospitalName: 'Sunrise Multispeciality',

      distance: '7.8 km',

      requiredBy: 'Within 6 hours'

    },

    {

      level: 'STANDARD',

      status: 'Open',

      bloodGroup: 'O-',

      unitsRequired: 3,

      hospitalName: 'Government General Blood Bank',

      distance: '11.5 km',

      requiredBy: 'Within 24 hours'

    }

  ];


  // =====================================================
  // RECENT DONATIONS
  // =====================================================

  recentDonations: Donation[] = [

    {

      date: '12 Jun 2026',

      hospital: 'City Care Hospital',

      bloodGroup: 'O+',

      units: 1,

      status: 'VERIFIED'

    },

    {

      date: '04 Feb 2026',

      hospital: 'Red Cross Blood Bank',

      bloodGroup: 'O+',

      units: 1,

      status: 'VERIFIED'

    },

    {

      date: '21 Sep 2025',

      hospital: 'Sunrise Multispeciality',

      bloodGroup: 'O+',

      units: 1,

      status: 'PENDING'

    }

  ];


  // =====================================================
  // NOTIFICATIONS
  // =====================================================

  notifications: Notification[] = [

    {

      message:
        'An emergency blood request matching your blood group is nearby.',

      time: '30 Aug',

      unread: true

    },

    {

      message:
        'Your recent donation has been verified.',

      time: '13 Jun',

      unread: true

    },

    {

      message:
        'Your donor availability has been updated.',

      time: '01 Jun',

      unread: false

    }

  ];


  // =====================================================
  // CONSTRUCTOR
  // =====================================================

  constructor() {

    addIcons({

      waterOutline,

      notificationsOutline,

      personCircleOutline,

      locationOutline,

      alertCircleOutline,

      businessOutline,

      calendarOutline,

      checkmarkCircleOutline,

      chevronForwardOutline,

      documentTextOutline,

      settingsOutline,

      personOutline,

      homeOutline,

      medicalOutline,

      heartOutline,

      closeOutline,

      timeOutline

    });

  }


  // =====================================================
  // INITIALIZATION
  // =====================================================

  ngOnInit(): void {

    this.loadLoggedInDonor();

  }


  // =====================================================
  // LOAD LOGGED-IN DONOR
  // =====================================================

  private loadLoggedInDonor(): void {

    const loggedInUser =

      localStorage.getItem(
        'bloodlink_logged_user'
      );


    // ---------------------------------------------------
    // NO USER FOUND
    // ---------------------------------------------------

    if (!loggedInUser) {

      console.warn(
        'No logged-in donor found.'
      );

      return;

    }


    // ---------------------------------------------------
    // READ USER
    // ---------------------------------------------------

    try {

      const user = JSON.parse(
        loggedInUser
      );


      // -------------------------------------------------
      // NAME
      // -------------------------------------------------

      this.donorName =

        user.fullName ||

        user.name ||

        'Donor';


      // -------------------------------------------------
      // EMAIL
      // -------------------------------------------------

      this.donorEmail =

        user.email ||

        '';


      // -------------------------------------------------
      // BLOOD GROUP
      // -------------------------------------------------

      this.bloodGroup =

        user.bloodGroup ||

        'O+';


      // -------------------------------------------------
      // CITY
      // -------------------------------------------------

      this.city =

        user.city ||

        'Hyderabad';


      console.log(
        'Logged-in donor:',
        user
      );

    }

    catch (error) {

      console.error(

        'Unable to read logged-in donor:',

        error

      );

    }

  }


  // =====================================================
  // NOTIFICATIONS
  // =====================================================

  openNotifications(): void {

    console.log(
      'Opening notifications'
    );

  }


  // =====================================================
  // PROFILE
  // =====================================================

  openProfile(): void {

    console.log(
      'Opening donor profile'
    );

  }


  // =====================================================
  // AVAILABILITY
  // =====================================================

  toggleAvailability(): void {

    this.isAvailable =

      !this.isAvailable;


    console.log(

      'Donor availability:',

      this.isAvailable

        ? 'Available'

        : 'Unavailable'

    );

  }


  // =====================================================
  // EMERGENCY REQUESTS
  // =====================================================

  openEmergencyRequests(): void {

    console.log(
      'Opening emergency requests'
    );

  }


  respondToRequest(
    request: EmergencyRequest
  ): void {

    console.log(

      'Responding to request:',

      request

    );

  }


  // =====================================================
  // DONATION HISTORY
  // =====================================================

  viewDonationHistory(): void {

    console.log(
      'Opening donation history'
    );

  }


  openDonationHistory(): void {

    console.log(
      'Opening donation history'
    );

  }


  // =====================================================
  // QUICK ACTIONS
  // =====================================================

  openAvailability(): void {

    console.log(
      'Opening availability settings'
    );

  }


  openBloodBanks(): void {

    console.log(
      'Opening nearby blood banks'
    );

  }


  // =====================================================
  // BOTTOM NAVIGATION
  // =====================================================

  navigateHome(): void {

    console.log(
      'Navigating to Home'
    );

  }


  navigateRequests(): void {

    console.log(
      'Navigating to Requests'
    );

  }


  navigateDonations(): void {

    console.log(
      'Navigating to Donations'
    );

  }


  navigateNotifications(): void {

    console.log(
      'Navigating to Notifications'
    );

  }


  navigateProfile(): void {

    console.log(
      'Navigating to Profile'
    );

  }

}