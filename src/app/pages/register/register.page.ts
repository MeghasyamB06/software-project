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
  eyeOffOutline,
  businessOutline,
  locationOutline
} from 'ionicons/icons';

import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';


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


  // ==========================================
  // MAXIMUM DATE OF BIRTH
  // ==========================================

  maxDate = new Date().toISOString().split('T')[0];


  // ==========================================
  // SELECTED ROLE
  // ==========================================

  selectedRole = 'DONOR';


  // ==========================================
  // REGISTRATION FORM DATA
  // ==========================================

  registration = {

    role: 'DONOR',

    fullName: '',

    email: '',

    mobile: '',

    dateOfBirth: '',

    gender: '',

    bloodGroup: '',

    password: '',

    confirmPassword: '',

    agreedToTerms: false,

    // Hospital / Blood Bank information
    organizationName: '',

    licenseNumber: '',

    address: '',

    city: ''

  };


  // ==========================================
  // PASSWORD VISIBILITY
  // ==========================================

  showPassword = false;

  showConfirmPassword = false;


  // ==========================================
  // LOADING STATE
  // ==========================================

  isLoading = false;


  // ==========================================
  // CONSTRUCTOR
  // ==========================================

  constructor(
    private router: Router,
    private authService: AuthService
  ) {

    // Register all Ionic icons used by the page
    addIcons({

      personOutline,

      mailOutline,

      callOutline,

      calendarOutline,

      waterOutline,

      lockClosedOutline,

      eyeOutline,

      eyeOffOutline,

      businessOutline,

      locationOutline

    });

  }


  // ==========================================
  // CHANGE ACCOUNT ROLE
  // ==========================================

  onRoleChange(): void {

    // Keep registration.role synchronized
    // with the selected role
    this.registration.role = this.selectedRole;


    // ==========================================
    // DONOR
    // ==========================================

    if (this.selectedRole === 'DONOR') {

      this.registration.organizationName = '';

      this.registration.licenseNumber = '';

      this.registration.address = '';

      this.registration.city = '';

    }


    // ==========================================
    // PATIENT
    // ==========================================

    if (this.selectedRole === 'PATIENT') {

      this.registration.organizationName = '';

      this.registration.licenseNumber = '';

    }


    // ==========================================
    // HOSPITAL / BLOOD BANK
    // ==========================================

    if (
      this.selectedRole === 'HOSPITAL' ||
      this.selectedRole === 'BLOOD_BANK'
    ) {

      // Clear donor-specific information
      this.registration.dateOfBirth = '';

      this.registration.gender = '';

      this.registration.bloodGroup = '';

    }

  }


  // ==========================================
  // ROLE SUBTITLE
  // ==========================================

  getRoleSubtitle(): string {

    switch (this.selectedRole) {

      case 'DONOR':

        return 'Register as a blood donor';


      case 'PATIENT':

        return 'Register as a patient or family member';


      case 'HOSPITAL':

        return 'Register your hospital';


      case 'BLOOD_BANK':

        return 'Register your blood bank';


      default:

        return 'Create your BloodLink account';

    }

  }


  // ==========================================
  // NAME FIELD LABEL
  // ==========================================

  getNameLabel(): string {

    switch (this.selectedRole) {

      case 'HOSPITAL':

        return 'Hospital Name';


      case 'BLOOD_BANK':

        return 'Blood Bank Name';


      default:

        return 'Full Name';

    }

  }


  // ==========================================
  // NAME FIELD PLACEHOLDER
  // ==========================================

  getNamePlaceholder(): string {

    switch (this.selectedRole) {

      case 'HOSPITAL':

        return 'Enter hospital name';


      case 'BLOOD_BANK':

        return 'Enter blood bank name';


      default:

        return 'Enter your full name';

    }

  }


  // ==========================================
  // CHECK INDIVIDUAL ROLE
  // ==========================================

  isIndividualRole(): boolean {

    return (
      this.selectedRole === 'DONOR' ||
      this.selectedRole === 'PATIENT'
    );

  }


  // ==========================================
  // CHECK ORGANIZATION ROLE
  // ==========================================

  isOrganizationRole(): boolean {

    return (
      this.selectedRole === 'HOSPITAL' ||
      this.selectedRole === 'BLOOD_BANK'
    );

  }


  // ==========================================
  // TOGGLE PASSWORD VISIBILITY
  // ==========================================

  togglePasswordVisibility(): void {

    this.showPassword = !this.showPassword;

  }


  // ==========================================
  // TOGGLE CONFIRM PASSWORD VISIBILITY
  // ==========================================

  toggleConfirmPasswordVisibility(): void {

    this.showConfirmPassword =
      !this.showConfirmPassword;

  }


  // ==========================================
  // REGISTER ACCOUNT
  // ==========================================

  onRegister(): void {

    // ------------------------------------------
    // Prevent multiple submissions
    // ------------------------------------------

    if (this.isLoading) {

      return;

    }


    // ------------------------------------------
    // Synchronize role
    // ------------------------------------------

    this.registration.role = this.selectedRole;


    // ==========================================
    // PASSWORD VALIDATION
    // ==========================================

    if (
      this.registration.password !==
      this.registration.confirmPassword
    ) {

      alert('Passwords do not match');

      return;

    }


    // ==========================================
    // TERMS AND CONDITIONS VALIDATION
    // ==========================================

    if (!this.registration.agreedToTerms) {

      alert('Please accept the Terms and Conditions');

      return;

    }


    // ==========================================
    // BASIC COMMON FIELD VALIDATION
    // ==========================================

    if (
      !this.registration.email ||
      !this.registration.mobile ||
      !this.registration.password
    ) {

      alert('Please complete all required information');

      return;

    }


    // ==========================================
    // DONOR VALIDATION
    // ==========================================

    if (this.selectedRole === 'DONOR') {

      if (
        !this.registration.fullName ||
        !this.registration.dateOfBirth ||
        !this.registration.gender ||
        !this.registration.bloodGroup
      ) {

        alert('Please complete all donor information');

        return;

      }

    }


    // ==========================================
    // ORGANIZATION VALIDATION
    // HOSPITAL / BLOOD BANK
    // ==========================================

    if (this.isOrganizationRole()) {

      /*
       * The Hospital/Blood Bank name is entered
       * in the common "fullName" field.
       *
       * Therefore we validate fullName instead of
       * organizationName.
       */

      if (
        !this.registration.fullName ||
        !this.registration.licenseNumber ||
        !this.registration.address ||
        !this.registration.city
      ) {

        alert('Please complete all organization information');

        return;

      }


      /*
       * Copy the Hospital/Blood Bank name into
       * organizationName before sending it to
       * the backend.
       */

      this.registration.organizationName =
        this.registration.fullName;

    }


    // ==========================================
    // START LOADING
    // ==========================================

    this.isLoading = true;


    // ==========================================
    // CREATE REGISTRATION REQUEST
    // ==========================================

    const user = {
      

      email: this.registration.email,

      mobile: this.registration.mobile,

      password: this.registration.password,

      role: this.registration.role,

      fullName:
    this.isOrganizationRole()
      ? this.registration.organizationName
      : this.registration.fullName,

      dateOfBirth: this.registration.dateOfBirth,

      gender: this.registration.gender,

      bloodGroup: this.registration.bloodGroup,

      organizationName: this.registration.organizationName,

      licenseNumber: this.registration.licenseNumber,

      address: this.registration.address,

      city: this.registration.city

    };


    // ==========================================
    // DEBUG
    // ==========================================

    console.log(
      'Sending registration data:',
      user
    );


    // ==========================================
    // SEND REQUEST TO BACKEND
    // ==========================================

    this.authService.register(user).subscribe({

      // ----------------------------------------
      // SUCCESS
      // ----------------------------------------

      next: (response) => {

        console.log(
          'Registration response:',
          response
        );


        this.isLoading = false;


        alert(response);


        // --------------------------------------
        // Navigate to Login
        // --------------------------------------

        if (response === 'Registration successful') {

  // Save the profile locally so the dashboard
  // can display the donor's actual name after login.

  const profileKey =
    `bloodlink_profile_${this.registration.email.toLowerCase()}`;

  const profile = {

    fullName:
      this.registration.fullName,

    email:
      this.registration.email,

    mobile:
      this.registration.mobile,

    bloodGroup:
      this.registration.bloodGroup,

    city:
      this.registration.city

  };


  localStorage.setItem(

    profileKey,

    JSON.stringify(profile)

  );


  this.router.navigate(['/login']);

}

      },


      // ----------------------------------------
      // ERROR
      // ----------------------------------------

      error: (error) => {

        console.error(
          'Registration error:',
          error
        );


        this.isLoading = false;


        // --------------------------------------
        // Show backend error
        // --------------------------------------

        if (error.error) {

          alert(error.error);

        } else {

          alert(
            'Registration failed. Please try again.'
          );

        }

      }

    });

  }


  // ==========================================
  // GO TO LOGIN
  // ==========================================

  onLogin(): void {

    this.router.navigate(['/login']);

  }


  // ==========================================
  // TERMS AND CONDITIONS
  // ==========================================

  onTerms(): void {

    console.log(
      'Terms and Conditions clicked'
    );

  }


  // ==========================================
  // PRIVACY POLICY
  // ==========================================

  onPrivacy(): void {

    console.log(
      'Privacy Policy clicked'
    );

  }

}