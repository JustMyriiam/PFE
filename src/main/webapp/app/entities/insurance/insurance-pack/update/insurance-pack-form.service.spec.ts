import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../insurance-pack.test-samples';

import { InsurancePackFormService } from './insurance-pack-form.service';

describe('InsurancePack Form Service', () => {
  let service: InsurancePackFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InsurancePackFormService);
  });

  describe('Service methods', () => {
    describe('createInsurancePackFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInsurancePackFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            desciption: expect.any(Object),
            price: expect.any(Object),
            warranties: expect.any(Object),
            vehicle: expect.any(Object),
          }),
        );
      });

      it('passing IInsurancePack should create a new form with FormGroup', () => {
        const formGroup = service.createInsurancePackFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            desciption: expect.any(Object),
            price: expect.any(Object),
            warranties: expect.any(Object),
            vehicle: expect.any(Object),
          }),
        );
      });
    });

    describe('getInsurancePack', () => {
      it('should return NewInsurancePack for default InsurancePack initial value', () => {
        const formGroup = service.createInsurancePackFormGroup(sampleWithNewData);

        const insurancePack = service.getInsurancePack(formGroup) as any;

        expect(insurancePack).toMatchObject(sampleWithNewData);
      });

      it('should return NewInsurancePack for empty InsurancePack initial value', () => {
        const formGroup = service.createInsurancePackFormGroup();

        const insurancePack = service.getInsurancePack(formGroup) as any;

        expect(insurancePack).toMatchObject({});
      });

      it('should return IInsurancePack', () => {
        const formGroup = service.createInsurancePackFormGroup(sampleWithRequiredData);

        const insurancePack = service.getInsurancePack(formGroup) as any;

        expect(insurancePack).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInsurancePack should not enable id FormControl', () => {
        const formGroup = service.createInsurancePackFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInsurancePack should disable id FormControl', () => {
        const formGroup = service.createInsurancePackFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
