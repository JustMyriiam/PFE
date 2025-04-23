import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClientAddressDetailComponent } from './client-address-detail.component';

describe('ClientAddress Management Detail Component', () => {
  let comp: ClientAddressDetailComponent;
  let fixture: ComponentFixture<ClientAddressDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientAddressDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./client-address-detail.component').then(m => m.ClientAddressDetailComponent),
              resolve: { clientAddress: () => of({ id: 'ba359270-b697-420f-9c86-ece18688b5ef' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ClientAddressDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientAddressDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load clientAddress on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ClientAddressDetailComponent);

      // THEN
      expect(instance.clientAddress()).toEqual(expect.objectContaining({ id: 'ba359270-b697-420f-9c86-ece18688b5ef' }));
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
