import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClaimDetailComponent } from './claim-detail.component';

describe('Claim Management Detail Component', () => {
  let comp: ClaimDetailComponent;
  let fixture: ComponentFixture<ClaimDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClaimDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./claim-detail.component').then(m => m.ClaimDetailComponent),
              resolve: { claim: () => of({ id: '94ec77c2-f5a1-41a3-b8f8-7bf9e231e536' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ClaimDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClaimDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load claim on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ClaimDetailComponent);

      // THEN
      expect(instance.claim()).toEqual(expect.objectContaining({ id: '94ec77c2-f5a1-41a3-b8f8-7bf9e231e536' }));
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
