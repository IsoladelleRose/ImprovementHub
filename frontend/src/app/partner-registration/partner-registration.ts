import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-partner-registration',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './partner-registration.html',
  styleUrl: './partner-registration.scss'
})
export class PartnerRegistration {
  innovationAreas = '';
  industries = '';
  vision = '';
  contributions = '';

  constructor(private router: Router) {}

  navigateToPartnerDetails() {
    // Validate required fields
    if (!this.innovationAreas.trim()) {
      alert('Please describe which areas of innovation or improvement you are most interested in.');
      return;
    }

    if (!this.industries.trim()) {
      alert('Please describe the specific industries or markets you are looking to expand into.');
      return;
    }

    if (!this.vision.trim()) {
      alert('Please describe your long-term vision for innovation and growth.');
      return;
    }

    if (!this.contributions.trim()) {
      alert('Please describe what you bring to the table in terms of competences, experience, and resources.');
      return;
    }

    // Store the partner data in localStorage to pass to the next step
    const partnerData = {
      innovationAreas: this.innovationAreas,
      industries: this.industries,
      vision: this.vision,
      contributions: this.contributions
    };
    localStorage.setItem('partnerData', JSON.stringify(partnerData));
    this.router.navigate(['/partner-details']);
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
