import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { InsurancePackDetailComponent } from './insurance-pack-detail.component';

describe('InsurancePack Management Detail Component', () => {
  let comp: InsurancePackDetailComponent;
  let fixture: ComponentFixture<InsurancePackDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InsurancePackDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./insurance-pack-detail.component').then(m => m.InsurancePackDetailComponent),
              resolve: { insurancePack: () => of({ id: '8d9bc0f2-5eaa-49fe-9c77-4ff4dd0af561' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InsurancePackDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InsurancePackDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load insurancePack on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InsurancePackDetailComponent);

      // THEN
      expect(instance.insurancePack()).toEqual(expect.objectContaining({ id: '8d9bc0f2-5eaa-49fe-9c77-4ff4dd0af561' }));
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
