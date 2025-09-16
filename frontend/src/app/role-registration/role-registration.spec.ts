import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleRegistration } from './role-registration';

describe('RoleRegistration', () => {
  let component: RoleRegistration;
  let fixture: ComponentFixture<RoleRegistration>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoleRegistration]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoleRegistration);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
