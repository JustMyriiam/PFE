import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../client.test-samples';

import { ClientFormService } from './client-form.service';

describe('Client Form Service', () => {
  let service: ClientFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientFormService);
  });

  describe('Service methods', () => {
    describe('createClientFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createClientFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            lastName: expect.any(Object),
            firstName: expect.any(Object),
            identityType: expect.any(Object),
            identityNumber: expect.any(Object),
            identityEmissionDate: expect.any(Object),
            birthDate: expect.any(Object),
            birthPlace: expect.any(Object),
            identityIssueDate: expect.any(Object),
            identityPlaceOfIssue: expect.any(Object),
            maritalStatus: expect.any(Object),
            nbrOfchildren: expect.any(Object),
            professionalEmail: expect.any(Object),
            personalEmail: expect.any(Object),
            primaryPhoneNumber: expect.any(Object),
            secondaryPhoneNumber: expect.any(Object),
            faxNumber: expect.any(Object),
            nationality: expect.any(Object),
            gender: expect.any(Object),
            jobTitle: expect.any(Object),
            professionalStatus: expect.any(Object),
            bank: expect.any(Object),
            agency: expect.any(Object),
            rib: expect.any(Object),
            drivingLicenseNumber: expect.any(Object),
            drivingLicenseIssueDate: expect.any(Object),
            drivingLicenseCategory: expect.any(Object),
            user: expect.any(Object),
            clientAddress: expect.any(Object),
          }),
        );
      });

      it('passing IClient should create a new form with FormGroup', () => {
        const formGroup = service.createClientFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            lastName: expect.any(Object),
            firstName: expect.any(Object),
            identityType: expect.any(Object),
            identityNumber: expect.any(Object),
            identityEmissionDate: expect.any(Object),
            birthDate: expect.any(Object),
            birthPlace: expect.any(Object),
            identityIssueDate: expect.any(Object),
            identityPlaceOfIssue: expect.any(Object),
            maritalStatus: expect.any(Object),
            nbrOfchildren: expect.any(Object),
            professionalEmail: expect.any(Object),
            personalEmail: expect.any(Object),
            primaryPhoneNumber: expect.any(Object),
            secondaryPhoneNumber: expect.any(Object),
            faxNumber: expect.any(Object),
            nationality: expect.any(Object),
            gender: expect.any(Object),
            jobTitle: expect.any(Object),
            professionalStatus: expect.any(Object),
            bank: expect.any(Object),
            agency: expect.any(Object),
            rib: expect.any(Object),
            drivingLicenseNumber: expect.any(Object),
            drivingLicenseIssueDate: expect.any(Object),
            drivingLicenseCategory: expect.any(Object),
            user: expect.any(Object),
            clientAddress: expect.any(Object),
          }),
        );
      });
    });

    describe('getClient', () => {
      it('should return NewClient for default Client initial value', () => {
        const formGroup = service.createClientFormGroup(sampleWithNewData);

        const client = service.getClient(formGroup) as any;

        expect(client).toMatchObject(sampleWithNewData);
      });

      it('should return NewClient for empty Client initial value', () => {
        const formGroup = service.createClientFormGroup();

        const client = service.getClient(formGroup) as any;

        expect(client).toMatchObject({});
      });

      it('should return IClient', () => {
        const formGroup = service.createClientFormGroup(sampleWithRequiredData);

        const client = service.getClient(formGroup) as any;

        expect(client).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IClient should not enable id FormControl', () => {
        const formGroup = service.createClientFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewClient should disable id FormControl', () => {
        const formGroup = service.createClientFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
