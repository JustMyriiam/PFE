import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { GovernorateDetailComponent } from './governorate-detail.component';

describe('Governorate Management Detail Component', () => {
  let comp: GovernorateDetailComponent;
  let fixture: ComponentFixture<GovernorateDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GovernorateDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./governorate-detail.component').then(m => m.GovernorateDetailComponent),
              resolve: { governorate: () => of({ id: '06b5bd02-0f85-4d95-9111-578d64b53b5c' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(GovernorateDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GovernorateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load governorate on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', GovernorateDetailComponent);

      // THEN
      expect(instance.governorate()).toEqual(expect.objectContaining({ id: '06b5bd02-0f85-4d95-9111-578d64b53b5c' }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
