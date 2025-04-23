import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../vehicle.test-samples';

import { VehicleFormService } from './vehicle-form.service';

describe('Vehicle Form Service', () => {
  let service: VehicleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VehicleFormService);
  });

  describe('Service methods', () => {
    describe('createVehicleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVehicleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            registrationNumber: expect.any(Object),
            registrationType: expect.any(Object),
            firstRegistrationDate: expect.any(Object),
            technicalInspectionStatus: expect.any(Object),
            expirationDate: expect.any(Object),
            newValue: expect.any(Object),
            marketValue: expect.any(Object),
            brand: expect.any(Object),
            model: expect.any(Object),
            fiscalPower: expect.any(Object),
            chassisNumber: expect.any(Object),
            energy: expect.any(Object),
            genre: expect.any(Object),
            nbrOfSeats: expect.any(Object),
            nbrOfStandingPlaces: expect.any(Object),
            emptyWeight: expect.any(Object),
            payload: expect.any(Object),
            bonusMalus: expect.any(Object),
            vehicleAge: expect.any(Object),
            mileage: expect.any(Object),
            numberOfDoors: expect.any(Object),
            gearbox: expect.any(Object),
            color: expect.any(Object),
            usage: expect.any(Object),
            isNew: expect.any(Object),
            hasGarage: expect.any(Object),
            hasParking: expect.any(Object),
            hasAlarmSystem: expect.any(Object),
            hasSeatbeltAlarm: expect.any(Object),
            hasRearCamera: expect.any(Object),
            hasRearRadar: expect.any(Object),
            hasAbsSystem: expect.any(Object),
            hasGPS: expect.any(Object),
            hasAirbag: expect.any(Object),
            navette: expect.any(Object),
          }),
        );
      });

      it('passing IVehicle should create a new form with FormGroup', () => {
        const formGroup = service.createVehicleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            registrationNumber: expect.any(Object),
            registrationType: expect.any(Object),
            firstRegistrationDate: expect.any(Object),
            technicalInspectionStatus: expect.any(Object),
            expirationDate: expect.any(Object),
            newValue: expect.any(Object),
            marketValue: expect.any(Object),
            brand: expect.any(Object),
            model: expect.any(Object),
            fiscalPower: expect.any(Object),
            chassisNumber: expect.any(Object),
            energy: expect.any(Object),
            genre: expect.any(Object),
            nbrOfSeats: expect.any(Object),
            nbrOfStandingPlaces: expect.any(Object),
            emptyWeight: expect.any(Object),
            payload: expect.any(Object),
            bonusMalus: expect.any(Object),
            vehicleAge: expect.any(Object),
            mileage: expect.any(Object),
            numberOfDoors: expect.any(Object),
            gearbox: expect.any(Object),
            color: expect.any(Object),
            usage: expect.any(Object),
            isNew: expect.any(Object),
            hasGarage: expect.any(Object),
            hasParking: expect.any(Object),
            hasAlarmSystem: expect.any(Object),
            hasSeatbeltAlarm: expect.any(Object),
            hasRearCamera: expect.any(Object),
            hasRearRadar: expect.any(Object),
            hasAbsSystem: expect.any(Object),
            hasGPS: expect.any(Object),
            hasAirbag: expect.any(Object),
            navette: expect.any(Object),
          }),
        );
      });
    });

    describe('getVehicle', () => {
      it('should return NewVehicle for default Vehicle initial value', () => {
        const formGroup = service.createVehicleFormGroup(sampleWithNewData);

        const vehicle = service.getVehicle(formGroup) as any;

        expect(vehicle).toMatchObject(sampleWithNewData);
      });

      it('should return NewVehicle for empty Vehicle initial value', () => {
        const formGroup = service.createVehicleFormGroup();

        const vehicle = service.getVehicle(formGroup) as any;

        expect(vehicle).toMatchObject({});
      });

      it('should return IVehicle', () => {
        const formGroup = service.createVehicleFormGroup(sampleWithRequiredData);

        const vehicle = service.getVehicle(formGroup) as any;

        expect(vehicle).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVehicle should not enable id FormControl', () => {
        const formGroup = service.createVehicleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVehicle should disable id FormControl', () => {
        const formGroup = service.createVehicleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
