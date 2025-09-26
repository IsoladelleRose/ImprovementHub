import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AIAnalysisService } from '../services/ai-analysis.service';

@Component({
  selector: 'app-idea-registration',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './idea-registration.html',
  styleUrl: './idea-registration.scss'
})
export class IdeaRegistration {
  coreConcept = '';
  problemOpportunity = '';
  email = '';

  // AI Analysis state
  aiAnalysis = '';
  isAnalyzing = false;
  analysisError = '';
  aiAvailable = false;

  constructor(
    private router: Router,
    private aiAnalysisService: AIAnalysisService
  ) {
    this.checkAIAvailability();
  }

  async checkAIAvailability() {
    try {
      const status = await this.aiAnalysisService.getStatus().toPromise();
      this.aiAvailable = status?.available || false;
    } catch (error) {
      this.aiAvailable = false;
    }
  }

  async analyzeWithAI() {
    if (!this.coreConcept.trim()) {
      alert('Please enter the core concept of your idea before analyzing.');
      return;
    }

    if (!this.problemOpportunity.trim()) {
      alert('Please describe the problem or opportunity before analyzing.');
      return;
    }

    if (!this.email.trim()) {
      alert('Please enter your email address for the analysis.');
      return;
    }

    this.isAnalyzing = true;
    this.analysisError = '';
    this.aiAnalysis = '';

    try {
      const response = await this.aiAnalysisService.analyzeIdea({
        coreConcept: this.coreConcept,
        problemOpportunity: this.problemOpportunity,
        email: this.email
      }).toPromise();

      if (response && response.analysis) {
        this.aiAnalysis = response.analysis;
      } else {
        throw new Error('No analysis received');
      }
    } catch (error: any) {
      this.analysisError = 'Failed to analyze idea: ' + (error.error?.error || error.message || 'Unknown error');
    } finally {
      this.isAnalyzing = false;
    }
  }

  navigateToRoleRegistration() {
    // Validate required fields
    if (!this.coreConcept.trim()) {
      alert('Please enter the core concept of your idea.');
      return;
    }

    if (!this.problemOpportunity.trim()) {
      alert('Please describe what problem or opportunity your idea addresses.');
      return;
    }

    // Store the idea data in localStorage to pass to the next step
    const ideaData = {
      coreConcept: this.coreConcept,
      problemOpportunity: this.problemOpportunity,
      email: this.email,
      aiAnalysis: this.aiAnalysis
    };
    localStorage.setItem('ideaData', JSON.stringify(ideaData));
    this.router.navigate(['/role-registration']);
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
