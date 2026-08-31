import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  IonContent,
  IonButton,
  IonIcon,
  IonSpinner
} from '@ionic/angular/standalone';

import { addIcons } from 'ionicons';

import {
  mailOutline,
  lockClosedOutline,
  eyeOutline,
  eyeOffOutline
} from 'ionicons/icons';

import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';


@Component({
  selector: 'app-login',

  templateUrl: './login.page.html',

  styleUrls: ['./login.page.scss'],

  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    IonContent,
    IonButton,
    IonIcon,
    IonSpinner
  ]
})


export class LoginPage {


  // =====================================================
  // LOGIN CREDENTIALS
  // =====================================================

  credentials = {

    email: '',

    password: ''

  };


  // =====================================================
  // UI STATE
  // =====================================================

  showPassword = false;

  isLoading = false;


  // =====================================================
  // CONSTRUCTOR
  // =====================================================

  constructor(

    private router: Router,

    private authService: AuthService

  ) {

    addIcons({

      mailOutline,

      lockClosedOutline,

      eyeOutline,

      eyeOffOutline

    });

  }


  // =====================================================
  // PASSWORD VISIBILITY
  // =====================================================

  togglePasswordVisibility(): void {

    this.showPassword =
      !this.showPassword;

  }


  // =====================================================
  // LOGIN
  // =====================================================

  onLogin(): void {


    // ---------------------------------------------------
    // VALIDATION
    // ---------------------------------------------------

    if (

      !this.credentials.email ||

      !this.credentials.password ||

      this.isLoading

    ) {

      return;

    }


    this.isLoading = true;


    // ---------------------------------------------------
    // CALL BACKEND LOGIN
    // ---------------------------------------------------

    this.authService.login(

      this.credentials.email,

      this.credentials.password

    ).subscribe({

      // ================================================
      // SUCCESS
      // ================================================

      next: (response) => {

        console.log(
          'Login response:',
          response
        );


        this.isLoading = false;


        // ------------------------------------------------
        // CHECK RESPONSE
        // ------------------------------------------------

        if (

          !response ||

          response.message !== 'Login successful'

        ) {

          alert(
            response?.message ||
            'Invalid login response from server.'
          );

          return;

        }


        // ------------------------------------------------
        // CREATE CURRENT USER
        // ------------------------------------------------

        const loggedInUser = {

          userId:
            response.userId,

          email:
            response.email ||
            this.credentials.email,

          role:
            response.role,

          fullName:
            response.fullName ||
            'Donor',

          bloodGroup:
            'O+',

          city:
            'Hyderabad'

        };


        // ------------------------------------------------
        // SAVE CURRENT USER
        // ------------------------------------------------

        localStorage.setItem(

          'bloodlink_logged_user',

          JSON.stringify(
            loggedInUser
          )

        );


        console.log(
          'Logged-in user saved:',
          loggedInUser
        );


        // ------------------------------------------------
        // ROLE
        // ------------------------------------------------

        const role =
          response.role?.toUpperCase();


        // ------------------------------------------------
        // DONOR
        // ------------------------------------------------

        if (role === 'DONOR') {

          this.router.navigate(
            ['/donor-dashboard']
          );

          return;

        }


        // ------------------------------------------------
        // PATIENT
        // ------------------------------------------------

        if (role === 'PATIENT') {

          this.router.navigate(
            ['/patient-dashboard']
          );

          return;

        }


        // ------------------------------------------------
        // HOSPITAL
        // ------------------------------------------------

        if (role === 'HOSPITAL') {

          this.router.navigate(
            ['/hospital-dashboard']
          );

          return;

        }


        // ------------------------------------------------
        // BLOOD BANK
        // ------------------------------------------------

        if (role === 'BLOOD_BANK') {

          this.router.navigate(
            ['/bloodbank-dashboard']
          );

          return;

        }


        // ------------------------------------------------
        // UNKNOWN ROLE
        // ------------------------------------------------

        alert(
          'Unknown account role.'
        );

      },


      // ================================================
      // ERROR
      // ================================================

      error: (error) => {

        console.error(
          'Login error:',
          error
        );


        this.isLoading = false;


        let message =
          'Login failed. Please check your credentials.';


        if (error?.error) {

          if (
            typeof error.error === 'string'
          ) {

            message =
              error.error;

          }

          else if (
            error.error.message
          ) {

            message =
              error.error.message;

          }

        }


        alert(message);

      }

    });

  }


  // =====================================================
  // FORGOT PASSWORD
  // =====================================================

  onForgotPassword(): void {

    console.log(
      'Forgot password clicked'
    );

  }


  // =====================================================
  // GOOGLE LOGIN
  // =====================================================

  onGoogleSignIn(): void {

    console.log(
      'Google sign in clicked'
    );

  }


  // =====================================================
  // REGISTER
  // =====================================================

  onRegister(): void {

    this.router.navigate(
      ['/register']
    );

  }

}