import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IdeaRegistration } from './idea-registration';

describe('IdeaRegistration', () => {
  let component: IdeaRegistration;
  let fixture: ComponentFixture<IdeaRegistration>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IdeaRegistration]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IdeaRegistration);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
