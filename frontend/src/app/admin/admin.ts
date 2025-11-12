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

  // Matching data
  matchingResults: Map<number, any> = new Map();
  isMatching: Map<number, boolean> = new Map();

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

  matchPartners(ideaId: number) {
    this.isMatching.set(ideaId, true);
    this.http.get<any>(`${this.apiUrl}/matching/partners/${ideaId}`).subscribe({
      next: (response) => {
        this.matchingResults.set(ideaId, response);
        this.isMatching.set(ideaId, false);
        console.log('Matching results:', response);
      },
      error: (error) => {
        console.error('Error matching partners:', error);
        this.isMatching.set(ideaId, false);
        alert('Failed to match partners. Please try again.');
      }
    });
  }

  getMatchingResults(ideaId: number) {
    return this.matchingResults.get(ideaId);
  }

  isMatchingInProgress(ideaId: number): boolean {
    return this.isMatching.get(ideaId) || false;
  }

  logout() {
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }
}
