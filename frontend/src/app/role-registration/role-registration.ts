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
  helpRequestAnswered = false;
  email = '';
  emailError = '';
  userRole = '';
  isSubmitting = false;
  errorMessage = '';
  successMessage = '';
  aiAnalysis = '';
  isAnalyzing = false;

  // Data from previous step
  coreConcept = '';
  problemOpportunity = '';

  private apiUrl = 'https://improvementhub-production.up.railway.app/api';

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
    this.helpRequestAnswered = true;
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
    // Validate that we have idea data
    if (!this.coreConcept || !this.problemOpportunity) {
      this.errorMessage = 'Please complete the idea registration first';
      return;
    }

    this.isAnalyzing = true;
    this.errorMessage = '';
    this.aiAnalysis = '';

    const analysisRequest = {
      coreConcept: this.coreConcept,
      problemOpportunity: this.problemOpportunity
    };

    this.http.post(`${this.apiUrl}/ideas/analyze`, analysisRequest, { responseType: 'text' }).subscribe({
      next: (response) => {
        this.isAnalyzing = false;
        this.aiAnalysis = response;
      },
      error: (error) => {
        this.isAnalyzing = false;
        this.errorMessage = error.error || 'Failed to analyze idea';
      }
    });
  }

  onSubmit() {
    // Validate help request selection
    if (!this.helpRequestAnswered) {
      this.errorMessage = 'Please select whether you want us to help';
      return;
    }

    // Validate role field if user wants help
    if (this.showRoleQuestion && !this.userRole.trim()) {
      this.errorMessage = 'Please describe which role you want to take up yourself';
      return;
    }

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
    this.successMessage = '';

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

        // Show appropriate success message based on response
        this.successMessage = response.message || 'Idea submitted successfully!';

        // Navigate after a short delay to let user see the message
        setTimeout(() => {
          if (response.savedToDatabase) {
            // User wanted help - navigate to login
            this.router.navigate(['/login']);
          } else {
            // User didn't want help - navigate back to home
            this.router.navigate(['/']);
          }
        }, 3000);
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
