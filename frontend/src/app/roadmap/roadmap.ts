import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-roadmap',
  imports: [],
  templateUrl: './roadmap.html',
  styleUrl: './roadmap.scss'
})
export class Roadmap {
  activeSection = 'roadmap';

  constructor(private router: Router) {}

  navigateToProfile() {
    this.router.navigate(['/profile']);
  }

  navigateToIdeasStatus() {
    this.router.navigate(['/ideas-status']);
  }

  navigateToBilling() {
    // Navigate to profile page with billing section active
    this.router.navigate(['/profile'], { queryParams: { section: 'billing' } });
  }

  navigateToPassword() {
    // Navigate to profile page with password section active
    this.router.navigate(['/profile'], { queryParams: { section: 'passwords' } });
  }

  navigateToSettings() {
    // Navigate to profile page with settings section active
    this.router.navigate(['/profile'], { queryParams: { section: 'settings' } });
  }

  setActiveSection(section: string) {
    this.activeSection = section;
  }
}
