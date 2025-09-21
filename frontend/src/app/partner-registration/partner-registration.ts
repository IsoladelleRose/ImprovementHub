import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-partner-registration',
  imports: [],
  templateUrl: './partner-registration.html',
  styleUrl: './partner-registration.scss'
})
export class PartnerRegistration {
  constructor(private router: Router) {}

  navigateToPartnerDetails() {
    this.router.navigate(['/partner-details']);
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
