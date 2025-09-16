import { Component } from '@angular/core';

@Component({
  selector: 'app-role-registration',
  imports: [],
  templateUrl: './role-registration.html',
  styleUrl: './role-registration.scss'
})
export class RoleRegistration {
  showRoleQuestion = false;

  onHelpRequestChange(value: string) {
    this.showRoleQuestion = value === 'yes';
  }
}
