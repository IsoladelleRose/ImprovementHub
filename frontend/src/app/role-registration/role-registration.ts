import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-role-registration',
  imports: [CommonModule, FormsModule],
  templateUrl: './role-registration.html',
  styleUrl: './role-registration.scss'
})
export class RoleRegistration {
  showRoleQuestion = false;
  email = '';
  emailError = '';

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
}
