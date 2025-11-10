import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IdeasStatus } from './ideas-status';

describe('IdeasStatus', () => {
  let component: IdeasStatus;
  let fixture: ComponentFixture<IdeasStatus>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IdeasStatus]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IdeasStatus);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
