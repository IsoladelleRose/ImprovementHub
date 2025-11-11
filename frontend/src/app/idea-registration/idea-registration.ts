import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-idea-registration',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './idea-registration.html',
  styleUrl: './idea-registration.scss'
})
export class IdeaRegistration {
  title = '';
  coreConcept = '';
  problemOpportunity = '';

  constructor(private router: Router) {}

  navigateToRoleRegistration() {
    // Validate required fields
    if (!this.title.trim()) {
      alert('Please enter a title for your idea.');
      return;
    }

    if (!this.coreConcept.trim()) {
      alert('Please enter the core concept of your idea.');
      return;
    }

    if (!this.problemOpportunity.trim()) {
      alert('Please describe what problem or opportunity your idea addresses.');
      return;
    }

    // Store the idea data in localStorage to pass to the next step
    const ideaData = {
      title: this.title,
      coreConcept: this.coreConcept,
      problemOpportunity: this.problemOpportunity
    };
    localStorage.setItem('ideaData', JSON.stringify(ideaData));
    this.router.navigate(['/role-registration']);
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
