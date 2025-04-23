import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AgencyDetailComponent } from './agency-detail.component';

describe('Agency Management Detail Component', () => {
  let comp: AgencyDetailComponent;
  let fixture: ComponentFixture<AgencyDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgencyDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./agency-detail.component').then(m => m.AgencyDetailComponent),
              resolve: { agency: () => of({ id: '4b7ed6c2-7221-414a-93bf-e1ec02bf3053' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AgencyDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AgencyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load agency on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AgencyDetailComponent);

      // THEN
      expect(instance.agency()).toEqual(expect.objectContaining({ id: '4b7ed6c2-7221-414a-93bf-e1ec02bf3053' }));
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
