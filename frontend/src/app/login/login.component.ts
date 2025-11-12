import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  emailAddress = '';
  password = '';
  showPassword = false;
  errorMessage = '';
  isLoading = false;

  private apiUrl = 'https://improvementhub-production.up.railway.app/api';

  constructor(private router: Router, private http: HttpClient) {}

  onLogin() {
    if (!this.emailAddress || !this.password) {
      this.errorMessage = 'Please fill in all fields';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const loginData = {
      emailAddress: this.emailAddress,
      password: this.password
    };

    this.http.post<any>(`${this.apiUrl}/auth/login`, loginData).subscribe({
      next: (response) => {
        this.isLoading = false;
        // Store user data in localStorage
        localStorage.setItem('user', JSON.stringify(response));
        // Redirect to admin page if user is admin, otherwise to profile page
        if (this.emailAddress === 'admin') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/profile']);
        }
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'User not found';
      }
    });
  }

  onForgotPassword() {
    if (!this.emailAddress) {
      this.errorMessage = 'Please enter your email address first';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const forgotPasswordData = {
      emailAddress: this.emailAddress
    };

    this.http.post<any>(`${this.apiUrl}/auth/forgot-password`, forgotPasswordData).subscribe({
      next: (response) => {
        this.isLoading = false;
        alert('Password reset email sent! Check your email for the new password.');
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'Failed to send reset email';
      }
    });
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  navigateHome() {
    this.router.navigate(['/']);
  }

  navigateRegister() {
    this.router.navigate(['/register']);
  }
}