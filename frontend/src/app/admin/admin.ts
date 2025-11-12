import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-admin',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.scss'
})
export class AdminComponent implements OnInit {
  activeSection: any = 'ideas';

  // User data
  user: any = null;

  // Ideas  data
  allIdeas: any[] = [];
  ideasWantingHelp: any[] = [];
  isLoadingIdeas = false;

  // General error handling
  errorMessage = '';

  private apiUrl = 'https://improvementhub-production.up.railway.app/api';

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit() {
    this.loadUserProfile();
    this.loadAllIdeas();
    this.loadIdeasWantingHelp();
  }

  loadUserProfile() {
    const userData = localStorage.getItem('user');
    if (userData) {
      try {
        this.user = JSON.parse(userData);
      } catch (error) {
        console.error('Error parsing user data:', error);
      }
    }
  }

  loadAllIdeas() {
    this.isLoadingIdeas = true;
    this.http.get<any[]>(`${this.apiUrl}/ideas`).subscribe({
      next: (ideas) => {
        this.allIdeas = ideas;
        this.isLoadingIdeas = false;
      },
      error: (error) => {
        console.error('Error fetching all ideas:', error);
        this.isLoadingIdeas = false;
      }
    });
  }

  loadIdeasWantingHelp() {
    this.http.get<any[]>(`${this.apiUrl}/ideas/wants-help`).subscribe({
      next: (ideas) => {
        this.ideasWantingHelp = ideas;
      },
      error: (error) => {
        console.error('Error fetching ideas wanting help:', error);
      }
    });
  }

  setActiveSection(section: any) {
    this.activeSection = section;
    this.errorMessage = '';
  }

  logout() {
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }
}
