import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VehicleDetailComponent } from './vehicle-detail.component';

describe('Vehicle Management Detail Component', () => {
  let comp: VehicleDetailComponent;
  let fixture: ComponentFixture<VehicleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VehicleDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./vehicle-detail.component').then(m => m.VehicleDetailComponent),
              resolve: { vehicle: () => of({ id: '9bb49fc1-a9df-4adb-bc15-4a7db764498f' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VehicleDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VehicleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load vehicle on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VehicleDetailComponent);

      // THEN
      expect(instance.vehicle()).toEqual(expect.objectContaining({ id: '9bb49fc1-a9df-4adb-bc15-4a7db764498f' }));
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
