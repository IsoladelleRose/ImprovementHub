import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-partner-details',
  imports: [CommonModule, FormsModule],
  templateUrl: './partner-details.html',
  styleUrl: './partner-details.scss'
})
export class PartnerDetails implements OnInit {
  company = '';
  vatNumber = '';
  contactPerson = '';
  street = '';
  city = '';
  postalCode = '';
  country = '';
  email = '';
  emailError = '';

  // Additional fields for partner registration
  innovationInterests = '';
  industriesMarkets = '';
  visionGrowth = '';
  competencesResources = '';

  isSubmitting = false;
  errorMessage = '';

  private apiUrl = 'http://localhost:8080/api';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit() {
    // Get email from query parameters if available
    this.route.queryParams.subscribe(params => {
      if (params['email']) {
        this.email = params['email'];
      }
    });
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

  onLetsGo() {
    this.validateEmail();
    if (this.emailError) {
      return;
    }

    if (!this.company || !this.contactPerson || !this.email) {
      this.errorMessage = 'Please fill in all required fields';
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';

    const partnerData = {
      companyName: this.company,
      vatNumber: this.vatNumber,
      contactPerson: this.contactPerson,
      streetAddress: this.street,
      city: this.city,
      postalCode: this.postalCode,
      country: this.country,
      email: this.email,
      innovationInterests: this.innovationInterests,
      industriesMarkets: this.industriesMarkets,
      visionGrowth: this.visionGrowth,
      competencesResources: this.competencesResources
    };

    this.http.post<any>(`${this.apiUrl}/partners/register`, partnerData).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        alert('Partner registration successful! Check your email for login credentials.');
        this.router.navigate(['/login']);
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || 'Failed to register partner';
      }
    });
  }
}
