import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  IonContent,
  IonButton,
  IonIcon,
  IonSpinner,
  IonSelect,
  IonSelectOption,
  IonCheckbox
} from '@ionic/angular/standalone';

import { addIcons } from 'ionicons';

import {
  personOutline,
  mailOutline,
  callOutline,
  calendarOutline,
  waterOutline,
  lockClosedOutline,
  eyeOutline,
  eyeOffOutline
} from 'ionicons/icons';

import { Router } from '@angular/router';


@Component({
  selector: 'app-register',

  templateUrl: './register.page.html',

  styleUrls: ['./register.page.scss'],

  standalone: true,

  imports: [
    CommonModule,
    FormsModule,

    IonContent,
    IonButton,
    IonIcon,
    IonSpinner,
    IonSelect,
    IonSelectOption,
    IonCheckbox
  ]
})


export class RegisterPage {

  /*
   * Maximum allowed DOB.
   *
   * This automatically becomes today's date.
   * Therefore users cannot select a future date.
   */
  maxDate = new Date().toISOString().split('T')[0];


  /*
   * Registration form data
   */
  registration = {

    fullName: '',

    email: '',

    mobile: '',

    dateOfBirth: '',

    gender: '',

    bloodGroup: '',

    password: '',

    confirmPassword: '',

    agreedToTerms: false

  };


  /*
   * Password visibility
   */
  showPassword = false;

  showConfirmPassword = false;


  /*
   * Loading state
   */
  isLoading = false;


  constructor(
    private router: Router
  ) {

    /*
     * Register all Ionic icons used by the page.
     */
    addIcons({

      personOutline,

      mailOutline,

      callOutline,

      calendarOutline,

      waterOutline,

      lockClosedOutline,

      eyeOutline,

      eyeOffOutline

    });

  }


  /*
   * Toggle main password visibility
   */
  togglePasswordVisibility(): void {

    this.showPassword = !this.showPassword;

  }


  /*
   * Toggle confirm password visibility
   */
  toggleConfirmPasswordVisibility(): void {

    this.showConfirmPassword =
      !this.showConfirmPassword;

  }


  /*
   * Create account
   */
  onRegister(): void {

    /*
     * Prevent multiple submissions.
     */
    if (this.isLoading) {
      return;
    }


    /*
     * Check password confirmation.
     */
    if (
      this.registration.password !==
      this.registration.confirmPassword
    ) {

      console.log('Passwords do not match');

      return;

    }


    /*
     * Check terms.
     */
    if (!this.registration.agreedToTerms) {

      console.log('Terms and conditions not accepted');

      return;

    }


    this.isLoading = true;


    /*
     * Temporary registration logic.
     *
     * Later we will replace this with your backend/
     * authentication service.
     */
    console.log(
      'Registration data:',
      this.registration
    );


    /*
     * Temporary delay to simulate registration.
     */
    setTimeout(() => {

      this.isLoading = false;


      /*
       * After successful registration,
       * go to login page.
       */
      this.router.navigate(['/login']);

    }, 1500);

  }


  /*
   * Go back to Login page.
   */
  onLogin(): void {

    this.router.navigate(['/login']);

  }


  /*
   * Terms and Conditions
   *
   * For now this is only a placeholder.
   */
  onTerms(): void {

    console.log('Terms and Conditions clicked');

  }


  /*
   * Privacy Policy
   *
   * For now this is only a placeholder.
   */
  onPrivacy(): void {

    console.log('Privacy Policy clicked');

  }

}