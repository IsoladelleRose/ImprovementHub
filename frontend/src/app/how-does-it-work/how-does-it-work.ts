import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-how-does-it-work',
  imports: [],
  templateUrl: './how-does-it-work.html',
  styleUrl: './how-does-it-work.scss'
})
export class HowDoesItWork {
  constructor(private router: Router) {}

  navigateToPartnerRegistration() {
    this.router.navigate(['/partner-registration']);
  }

  navigateToIdeaRegistration() {
    this.router.navigate(['/idea-registration']);
  }
}
