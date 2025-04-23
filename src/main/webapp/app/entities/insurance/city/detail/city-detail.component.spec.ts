import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CityDetailComponent } from './city-detail.component';

describe('City Management Detail Component', () => {
  let comp: CityDetailComponent;
  let fixture: ComponentFixture<CityDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CityDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./city-detail.component').then(m => m.CityDetailComponent),
              resolve: { city: () => of({ id: '23758c6a-e922-4595-b3e2-be143d026188' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CityDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load city on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CityDetailComponent);

      // THEN
      expect(instance.city()).toEqual(expect.objectContaining({ id: '23758c6a-e922-4595-b3e2-be143d026188' }));
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
