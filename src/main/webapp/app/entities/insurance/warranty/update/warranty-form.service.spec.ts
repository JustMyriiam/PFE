import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../warranty.test-samples';

import { WarrantyFormService } from './warranty-form.service';

describe('Warranty Form Service', () => {
  let service: WarrantyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WarrantyFormService);
  });

  describe('Service methods', () => {
    describe('createWarrantyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWarrantyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            limit: expect.any(Object),
            franchise: expect.any(Object),
            price: expect.any(Object),
            mandatory: expect.any(Object),
            insurancePacks: expect.any(Object),
          }),
        );
      });

      it('passing IWarranty should create a new form with FormGroup', () => {
        const formGroup = service.createWarrantyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            limit: expect.any(Object),
            franchise: expect.any(Object),
            price: expect.any(Object),
            mandatory: expect.any(Object),
            insurancePacks: expect.any(Object),
          }),
        );
      });
    });

    describe('getWarranty', () => {
      it('should return NewWarranty for default Warranty initial value', () => {
        const formGroup = service.createWarrantyFormGroup(sampleWithNewData);

        const warranty = service.getWarranty(formGroup) as any;

        expect(warranty).toMatchObject(sampleWithNewData);
      });

      it('should return NewWarranty for empty Warranty initial value', () => {
        const formGroup = service.createWarrantyFormGroup();

        const warranty = service.getWarranty(formGroup) as any;

        expect(warranty).toMatchObject({});
      });

      it('should return IWarranty', () => {
        const formGroup = service.createWarrantyFormGroup(sampleWithRequiredData);

        const warranty = service.getWarranty(formGroup) as any;

        expect(warranty).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWarranty should not enable id FormControl', () => {
        const formGroup = service.createWarrantyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWarranty should disable id FormControl', () => {
        const formGroup = service.createWarrantyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
