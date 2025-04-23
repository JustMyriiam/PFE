import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../governorate.test-samples';

import { GovernorateFormService } from './governorate-form.service';

describe('Governorate Form Service', () => {
  let service: GovernorateFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GovernorateFormService);
  });

  describe('Service methods', () => {
    describe('createGovernorateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGovernorateFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IGovernorate should create a new form with FormGroup', () => {
        const formGroup = service.createGovernorateFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getGovernorate', () => {
      it('should return NewGovernorate for default Governorate initial value', () => {
        const formGroup = service.createGovernorateFormGroup(sampleWithNewData);

        const governorate = service.getGovernorate(formGroup) as any;

        expect(governorate).toMatchObject(sampleWithNewData);
      });

      it('should return NewGovernorate for empty Governorate initial value', () => {
        const formGroup = service.createGovernorateFormGroup();

        const governorate = service.getGovernorate(formGroup) as any;

        expect(governorate).toMatchObject({});
      });

      it('should return IGovernorate', () => {
        const formGroup = service.createGovernorateFormGroup(sampleWithRequiredData);

        const governorate = service.getGovernorate(formGroup) as any;

        expect(governorate).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGovernorate should not enable id FormControl', () => {
        const formGroup = service.createGovernorateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGovernorate should disable id FormControl', () => {
        const formGroup = service.createGovernorateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
