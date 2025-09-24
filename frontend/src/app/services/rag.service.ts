import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Document {
  id: number;
  title: string;
  content: string;
  fileName?: string;
  contentType?: string;
  fileSize?: number;
  uploadedAt: string;
}

export interface UploadTextRequest {
  title: string;
  content: string;
}

export interface QueryRequest {
  question: string;
}

@Injectable({
  providedIn: 'root'
})
export class RagService {
  private apiUrl = `${environment.apiUrl}/api/rag`;

  constructor(private http: HttpClient) {}

  uploadFile(file: File, title: string): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('title', title);

    return this.http.post(`${this.apiUrl}/upload`, formData, {
      responseType: 'text'
    });
  }

  uploadText(request: UploadTextRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/upload-text`, request, {
      responseType: 'text'
    });
  }

  query(request: QueryRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/query`, request, {
      responseType: 'text'
    });
  }

  getDocuments(): Observable<Document[]> {
    return this.http.get<Document[]>(`${this.apiUrl}/documents`);
  }

  searchDocuments(title: string): Observable<Document[]> {
    const params = new HttpParams().set('title', title);
    return this.http.get<Document[]>(`${this.apiUrl}/documents/search`, { params });
  }

  deleteDocument(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/documents/${id}`, {
      responseType: 'text'
    });
  }

  getStats(): Observable<string> {
    return this.http.get(`${this.apiUrl}/stats`, {
      responseType: 'text'
    });
  }

  healthCheck(): Observable<string> {
    return this.http.get(`${this.apiUrl}/health`, {
      responseType: 'text'
    });
  }
}