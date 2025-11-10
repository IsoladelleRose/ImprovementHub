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

  setActiveSection(section: string) {
    this.activeSection = section;
  }
}
