import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverviewInnovator } from './overview-innovator';

describe('OverviewInnovator', () => {
  let component: OverviewInnovator;
  let fixture: ComponentFixture<OverviewInnovator>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OverviewInnovator]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OverviewInnovator);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
