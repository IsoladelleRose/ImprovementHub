import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-partner-details',
  imports: [CommonModule, FormsModule],
  templateUrl: './partner-details.html',
  styleUrl: './partner-details.scss'
})
export class PartnerDetails {
  company = '';
  vatNumber = '';
  contactPerson = '';
  street = '';
  city = '';
  postalCode = '';
  country = '';
  email = '';
  emailError = '';

  validateEmail() {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!this.email) {
      this.emailError = 'Email address is required';
    } else if (!emailPattern.test(this.email)) {
      this.emailError = 'Please enter a valid email address';
    } else {
      this.emailError = '';
    }
  }
}
