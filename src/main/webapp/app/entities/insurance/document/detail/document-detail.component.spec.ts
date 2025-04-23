import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DocumentDetailComponent } from './document-detail.component';

describe('Document Management Detail Component', () => {
  let comp: DocumentDetailComponent;
  let fixture: ComponentFixture<DocumentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DocumentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./document-detail.component').then(m => m.DocumentDetailComponent),
              resolve: { document: () => of({ id: 'c1d4f1eb-eff0-4815-be04-c0d821e59542' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DocumentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load document on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DocumentDetailComponent);

      // THEN
      expect(instance.document()).toEqual(expect.objectContaining({ id: 'c1d4f1eb-eff0-4815-be04-c0d821e59542' }));
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
