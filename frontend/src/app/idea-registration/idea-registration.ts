import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-idea-registration',
  imports: [],
  templateUrl: './idea-registration.html',
  styleUrl: './idea-registration.scss'
})
export class IdeaRegistration {
  constructor(private router: Router) {}

  navigateToRoleRegistration() {
    this.router.navigate(['/role-registration']);
  }
}
