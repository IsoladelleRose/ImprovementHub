import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-idea-registration',
  imports: [CommonModule, FormsModule],
  templateUrl: './idea-registration.html',
  styleUrl: './idea-registration.scss'
})
export class IdeaRegistration implements OnInit {
  email = '';
  coreConcept = '';
  problemOpportunity = '';
  wantsHelp = false;
  userRole = '';
  isSubmitting = false;
  errorMessage = '';

  private apiUrl = 'http://localhost:8080/api';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit() {
    // Get email from query parameters if available
    this.route.queryParams.subscribe(params => {
      if (params['email']) {
        this.email = params['email'];
      }
    });
  }

  onSubmitIdea() {
    if (!this.coreConcept || !this.problemOpportunity || !this.email) {
      this.errorMessage = 'Please fill in all required fields';
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';

    const ideaData = {
      email: this.email,
      coreConcept: this.coreConcept,
      problemOpportunity: this.problemOpportunity,
      wantsHelp: this.wantsHelp,
      userRole: this.userRole
    };

    this.http.post<any>(`${this.apiUrl}/ideas/register`, ideaData).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        alert('Idea submitted successfully! Check your email for login credentials.');
        this.router.navigate(['/login']);
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || 'Failed to submit idea';
      }
    });
  }

  navigateToRoleRegistration() {
    this.router.navigate(['/role-registration']);
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
