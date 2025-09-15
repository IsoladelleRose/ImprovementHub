import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartnerRegistration } from './partner-registration';

describe('PartnerRegistration', () => {
  let component: PartnerRegistration;
  let fixture: ComponentFixture<PartnerRegistration>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PartnerRegistration]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PartnerRegistration);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
