import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-role-registration',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './role-registration.html',
  styleUrl: './role-registration.scss'
})
export class RoleRegistration implements OnInit {
  showRoleQuestion = false;
  email = '';
  emailError = '';
  userRole = '';
  isSubmitting = false;
  errorMessage = '';

  // Data from previous step
  coreConcept = '';
  problemOpportunity = '';

  private apiUrl = 'http://localhost:8080/api';

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit() {
    // Get idea data from localStorage from the previous step
    const ideaData = localStorage.getItem('ideaData');
    if (ideaData) {
      const data = JSON.parse(ideaData);
      this.coreConcept = data.coreConcept;
      this.problemOpportunity = data.problemOpportunity;
    }
  }

  onHelpRequestChange(value: string) {
    this.showRoleQuestion = value === 'yes';
  }

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

  onAnalyze() {
    this.validateEmail();
    if (this.emailError) {
      return;
    }

    if (!this.coreConcept || !this.problemOpportunity) {
      this.errorMessage = 'Please complete the idea registration first';
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';

    const ideaData = {
      email: this.email,
      coreConcept: this.coreConcept,
      problemOpportunity: this.problemOpportunity,
      wantsHelp: this.showRoleQuestion,
      userRole: this.userRole
    };

    this.http.post<any>(`${this.apiUrl}/ideas/register`, ideaData).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        // Clear the stored idea data
        localStorage.removeItem('ideaData');

        // Show appropriate message based on response
        alert(response.message || 'Idea submitted successfully!');

        // Navigate based on whether they wanted help or not
        if (response.savedToDatabase) {
          // User wanted help - navigate to login
          this.router.navigate(['/login']);
        } else {
          // User didn't want help - navigate back to home
          this.router.navigate(['/']);
        }
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || 'Failed to submit idea';
      }
    });
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
