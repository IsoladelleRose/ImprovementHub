import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AIAnalysisRequest {
  coreConcept: string;
  problemOpportunity: string;
  email: string;
}

export interface AIAnalysisResponse {
  analysis: string;
  timestamp: number;
  status: string;
}

export interface AIStatusResponse {
  available: boolean;
  service: string;
  timestamp: number;
}

@Injectable({
  providedIn: 'root'
})
export class AIAnalysisService {
  private apiUrl = `${environment.apiUrl}/api/ai`;

  constructor(private http: HttpClient) {}

  analyzeIdea(request: AIAnalysisRequest): Observable<AIAnalysisResponse> {
    return this.http.post<AIAnalysisResponse>(`${this.apiUrl}/analyze-idea`, request);
  }

  checkHealth(): Observable<string> {
    return this.http.get(`${this.apiUrl}/health`, { responseType: 'text' });
  }

  getStatus(): Observable<AIStatusResponse> {
    return this.http.get<AIStatusResponse>(`${this.apiUrl}/status`);
  }
}