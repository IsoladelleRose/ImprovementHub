import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RagService } from '../services/rag.service';

@Component({
  selector: 'app-rag',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './rag.component.html',
  styleUrls: ['./rag.component.scss']
})
export class RagComponent implements OnInit {

  // Query section
  question: string = '';
  answer: string = '';
  isQuerying: boolean = false;

  // Messages
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private ragService: RagService,
    private router: Router
  ) {}

  ngOnInit() {
    // Test the connection immediately
    this.testConnection();
  }

  async testConnection() {
    try {
      const healthResponse = await this.ragService.healthCheck().toPromise();
      this.showSuccess('RAG system connected successfully!');
      console.log('Health check response:', healthResponse);
    } catch (error: any) {
      this.showError('Failed to connect to RAG system: ' + (error.error?.message || error.message || 'Unknown error'));
      console.error('Health check failed:', error);
    }
  }

  // Query Methods
  async submitQuery() {
    if (!this.question.trim()) {
      this.showError('Please enter a question');
      return;
    }

    this.isQuerying = true;
    this.answer = '';
    this.clearMessages();

    try {
      console.log('Submitting query:', this.question);
      this.answer = await this.ragService.query({ question: this.question }).toPromise() || '';
      this.showSuccess('Answer received!');
    } catch (error: any) {
      console.error('Query failed:', error);
      this.showError('Failed to get answer: ' + (error.error?.message || error.message || 'Unknown error'));
    } finally {
      this.isQuerying = false;
    }
  }

  // Utility Methods
  showSuccess(message: string) {
    this.successMessage = message;
    this.errorMessage = '';
    setTimeout(() => this.successMessage = '', 5000);
  }

  showError(message: string) {
    this.errorMessage = message;
    this.successMessage = '';
  }

  clearMessages() {
    this.successMessage = '';
    this.errorMessage = '';
  }

  goHome() {
    this.router.navigate(['/dashboard']);
  }
}