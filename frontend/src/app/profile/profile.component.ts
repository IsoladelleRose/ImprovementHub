import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  // User data
  user: any = null;

  // Password change form
  currentPassword = '';
  newPassword = '';
  confirmNewPassword = '';
  passwordChangeError = '';
  passwordChangeSuccess = '';
  isChangingPassword = false;

  // General error handling
  errorMessage = '';

  // Active section
  activeSection = 'profile';

  private apiUrl = 'https://improvementhub-production.up.railway.app/api';

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit() {
    this.loadUserProfile();
  }

  loadUserProfile() {
    // Get user data from localStorage (set during login)
    const userData = localStorage.getItem('user');
    if (!userData) {
      // No user data, redirect to login
      this.router.navigate(['/login']);
      return;
    }

    try {
      this.user = JSON.parse(userData);
      // Optionally fetch fresh data from backend
      this.fetchUserProfile();
    } catch (error) {
      console.error('Error parsing user data:', error);
      this.router.navigate(['/login']);
    }
  }

  fetchUserProfile() {
    if (!this.user?.emailAddress) return;

    this.http.get<any>(`${this.apiUrl}/auth/profile/${this.user.emailAddress}`).subscribe({
      next: (response) => {
        this.user = { ...this.user, ...response };
        // Update localStorage with fresh data
        localStorage.setItem('user', JSON.stringify(this.user));
      },
      error: (error) => {
        console.error('Error fetching user profile:', error);
        this.errorMessage = 'Failed to load user profile';
      }
    });
  }

  onChangePassword() {
    // Reset messages
    this.passwordChangeError = '';
    this.passwordChangeSuccess = '';

    // Validation
    if (!this.currentPassword || !this.newPassword || !this.confirmNewPassword) {
      this.passwordChangeError = 'Please fill in all password fields';
      return;
    }

    if (this.newPassword !== this.confirmNewPassword) {
      this.passwordChangeError = 'New passwords do not match';
      return;
    }

    if (this.newPassword.length < 8) {
      this.passwordChangeError = 'New password must be at least 8 characters long';
      return;
    }

    // Password strength validation
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]/;
    if (!passwordRegex.test(this.newPassword)) {
      this.passwordChangeError = 'Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character';
      return;
    }

    this.isChangingPassword = true;

    const changePasswordData = {
      emailAddress: this.user.emailAddress,
      currentPassword: this.currentPassword,
      newPassword: this.newPassword,
      confirmNewPassword: this.confirmNewPassword
    };

    this.http.post<any>(`${this.apiUrl}/auth/change-password`, changePasswordData).subscribe({
      next: (response) => {
        this.isChangingPassword = false;
        this.passwordChangeSuccess = 'Password changed successfully';
        // Clear form
        this.currentPassword = '';
        this.newPassword = '';
        this.confirmNewPassword = '';
      },
      error: (error) => {
        this.isChangingPassword = false;
        this.passwordChangeError = error.error?.message || 'Failed to change password';
      }
    });
  }

  setActiveSection(section: string) {
    this.activeSection = section;
    // Clear any error messages when switching sections
    this.errorMessage = '';
    this.passwordChangeError = '';
    this.passwordChangeSuccess = '';
  }

  navigateHome() {
    this.router.navigate(['/']);
  }

  navigateToOverviewInnovator() {
    this.router.navigate(['/overview-innovator']);
  }

  logout() {
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }

  getUserRole(): string {
    if (!this.user) return '';

    const roles = [];
    if (this.user.inventor) roles.push('Inventor');
    if (this.user.innovator) roles.push('Innovator');

    return roles.length > 0 ? roles.join(' & ') : 'User';
  }

  getMemberSince(): string {
    if (!this.user?.createdAt) return '';

    const date = new Date(this.user.createdAt);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long'
    });
  }
}