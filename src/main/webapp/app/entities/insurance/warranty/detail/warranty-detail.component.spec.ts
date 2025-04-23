import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { WarrantyDetailComponent } from './warranty-detail.component';

describe('Warranty Management Detail Component', () => {
  let comp: WarrantyDetailComponent;
  let fixture: ComponentFixture<WarrantyDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WarrantyDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./warranty-detail.component').then(m => m.WarrantyDetailComponent),
              resolve: { warranty: () => of({ id: '02209a44-8170-48fc-bfe9-76651f25b708' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(WarrantyDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WarrantyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load warranty on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', WarrantyDetailComponent);

      // THEN
      expect(instance.warranty()).toEqual(expect.objectContaining({ id: '02209a44-8170-48fc-bfe9-76651f25b708' }));
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
