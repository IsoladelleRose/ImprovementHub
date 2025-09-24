import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RagService, Document } from '../services/rag.service';

@Component({
  selector: 'app-rag',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './rag.component.html',
  styleUrls: ['./rag.component.scss']
})
export class RagComponent implements OnInit {
  activeTab: 'query' | 'upload' | 'documents' = 'query';

  // Query section
  question: string = '';
  answer: string = '';
  isQuerying: boolean = false;

  // Upload section
  selectedFile: File | null = null;
  uploadTitle: string = '';
  textContent: string = '';
  uploadMode: 'file' | 'text' = 'text';
  isUploading: boolean = false;

  // Documents section
  documents: Document[] = [];
  searchTitle: string = '';
  isLoadingDocuments: boolean = false;

  // Messages
  successMessage: string = '';
  errorMessage: string = '';

  // Stats
  stats: string = '';

  constructor(
    private ragService: RagService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadDocuments();
    this.loadStats();
  }

  setActiveTab(tab: 'query' | 'upload' | 'documents') {
    this.activeTab = tab;
    this.clearMessages();

    if (tab === 'documents') {
      this.loadDocuments();
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
      this.answer = await this.ragService.query({ question: this.question }).toPromise() || '';
    } catch (error: any) {
      this.showError('Failed to get answer: ' + (error.error || error.message));
    } finally {
      this.isQuerying = false;
    }
  }

  // Upload Methods
  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      if (!this.uploadTitle) {
        this.uploadTitle = file.name.replace(/\.[^/.]+$/, '');
      }
    }
  }

  async uploadDocument() {
    if (this.uploadMode === 'file') {
      await this.uploadFile();
    } else {
      await this.uploadText();
    }
  }

  async uploadFile() {
    if (!this.selectedFile || !this.uploadTitle.trim()) {
      this.showError('Please select a file and enter a title');
      return;
    }

    this.isUploading = true;
    this.clearMessages();

    try {
      const response = await this.ragService.uploadFile(this.selectedFile, this.uploadTitle).toPromise();
      this.showSuccess(response || 'File uploaded successfully');
      this.resetUploadForm();
      this.loadDocuments();
      this.loadStats();
    } catch (error: any) {
      this.showError('Failed to upload file: ' + (error.error || error.message));
    } finally {
      this.isUploading = false;
    }
  }

  async uploadText() {
    if (!this.uploadTitle.trim() || !this.textContent.trim()) {
      this.showError('Please enter both title and content');
      return;
    }

    this.isUploading = true;
    this.clearMessages();

    try {
      const response = await this.ragService.uploadText({
        title: this.uploadTitle,
        content: this.textContent
      }).toPromise();
      this.showSuccess(response || 'Text uploaded successfully');
      this.resetUploadForm();
      this.loadDocuments();
      this.loadStats();
    } catch (error: any) {
      this.showError('Failed to upload text: ' + (error.error || error.message));
    } finally {
      this.isUploading = false;
    }
  }

  resetUploadForm() {
    this.selectedFile = null;
    this.uploadTitle = '';
    this.textContent = '';
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }

  // Documents Methods
  async loadDocuments() {
    this.isLoadingDocuments = true;

    try {
      this.documents = await this.ragService.getDocuments().toPromise() || [];
    } catch (error: any) {
      this.showError('Failed to load documents: ' + (error.error || error.message));
    } finally {
      this.isLoadingDocuments = false;
    }
  }

  async searchDocuments() {
    if (!this.searchTitle.trim()) {
      this.loadDocuments();
      return;
    }

    this.isLoadingDocuments = true;

    try {
      this.documents = await this.ragService.searchDocuments(this.searchTitle).toPromise() || [];
    } catch (error: any) {
      this.showError('Failed to search documents: ' + (error.error || error.message));
    } finally {
      this.isLoadingDocuments = false;
    }
  }

  async deleteDocument(id: number, title: string) {
    if (!confirm(`Are you sure you want to delete "${title}"?`)) {
      return;
    }

    try {
      const response = await this.ragService.deleteDocument(id).toPromise();
      this.showSuccess(response || 'Document deleted successfully');
      this.loadDocuments();
      this.loadStats();
    } catch (error: any) {
      this.showError('Failed to delete document: ' + (error.error || error.message));
    }
  }

  // Stats Methods
  async loadStats() {
    try {
      this.stats = await this.ragService.getStats().toPromise() || '';
    } catch (error: any) {
      console.error('Failed to load stats:', error);
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

  formatFileSize(bytes: number | undefined): string {
    if (!bytes) return 'Unknown';
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return Math.round(bytes / 1024) + ' KB';
    return Math.round(bytes / (1024 * 1024)) + ' MB';
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleString();
  }

  goHome() {
    this.router.navigate(['/dashboard']);
  }
}