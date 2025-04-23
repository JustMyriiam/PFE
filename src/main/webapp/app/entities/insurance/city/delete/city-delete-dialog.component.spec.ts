jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CityService } from '../service/city.service';

import { CityDeleteDialogComponent } from './city-delete-dialog.component';

describe('City Management Delete Component', () => {
  let comp: CityDeleteDialogComponent;
  let fixture: ComponentFixture<CityDeleteDialogComponent>;
  let service: CityService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CityDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(CityDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CityDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CityService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete('9fec3727-3421-4967-b213-ba36557ca194');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('9fec3727-3421-4967-b213-ba36557ca194');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
