import { Component } from '@angular/core';
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
export class RoleRegistration {
  showRoleQuestion = false;
  email = '';
  emailError = '';
  userRole = '';

  private apiUrl = 'http://localhost:8080/api';

  constructor(private router: Router, private http: HttpClient) {}

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

    // Navigate to idea registration with email parameter
    this.router.navigate(['/idea-registration'], {
      queryParams: { email: this.email }
    });
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
