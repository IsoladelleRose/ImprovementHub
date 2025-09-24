import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home {
  constructor(private router: Router) {}

  navigateToHowItWorks() {
    this.router.navigate(['/how-does-it-work']);
  }

  navigateToPartnerRegistration() {
    this.router.navigate(['/partner-registration']);
  }

  navigateToIdeaRegistration() {
    this.router.navigate(['/idea-registration']);
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }

  navigateToRag() {
    this.router.navigate(['/rag']);
  }
}
