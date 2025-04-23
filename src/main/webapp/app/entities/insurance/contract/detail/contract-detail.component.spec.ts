import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ContractDetailComponent } from './contract-detail.component';

describe('Contract Management Detail Component', () => {
  let comp: ContractDetailComponent;
  let fixture: ComponentFixture<ContractDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContractDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./contract-detail.component').then(m => m.ContractDetailComponent),
              resolve: { contract: () => of({ id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ContractDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ContractDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load contract on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ContractDetailComponent);

      // THEN
      expect(instance.contract()).toEqual(expect.objectContaining({ id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' }));
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
