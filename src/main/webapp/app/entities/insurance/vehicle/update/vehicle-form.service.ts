import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVehicle, NewVehicle } from '../vehicle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVehicle for edit and NewVehicleFormGroupInput for create.
 */
type VehicleFormGroupInput = IVehicle | PartialWithRequiredKeyOf<NewVehicle>;

type VehicleFormDefaults = Pick<
  NewVehicle,
  | 'id'
  | 'isNew'
  | 'hasGarage'
  | 'hasParking'
  | 'hasAlarmSystem'
  | 'hasSeatbeltAlarm'
  | 'hasRearCamera'
  | 'hasRearRadar'
  | 'hasAbsSystem'
  | 'hasGPS'
  | 'hasAirbag'
  | 'navette'
>;

type VehicleFormGroupContent = {
  id: FormControl<IVehicle['id'] | NewVehicle['id']>;
  registrationNumber: FormControl<IVehicle['registrationNumber']>;
  registrationType: FormControl<IVehicle['registrationType']>;
  firstRegistrationDate: FormControl<IVehicle['firstRegistrationDate']>;
  technicalInspectionStatus: FormControl<IVehicle['technicalInspectionStatus']>;
  expirationDate: FormControl<IVehicle['expirationDate']>;
  newValue: FormControl<IVehicle['newValue']>;
  marketValue: FormControl<IVehicle['marketValue']>;
  brand: FormControl<IVehicle['brand']>;
  model: FormControl<IVehicle['model']>;
  fiscalPower: FormControl<IVehicle['fiscalPower']>;
  chassisNumber: FormControl<IVehicle['chassisNumber']>;
  energy: FormControl<IVehicle['energy']>;
  genre: FormControl<IVehicle['genre']>;
  nbrOfSeats: FormControl<IVehicle['nbrOfSeats']>;
  nbrOfStandingPlaces: FormControl<IVehicle['nbrOfStandingPlaces']>;
  emptyWeight: FormControl<IVehicle['emptyWeight']>;
  payload: FormControl<IVehicle['payload']>;
  bonusMalus: FormControl<IVehicle['bonusMalus']>;
  vehicleAge: FormControl<IVehicle['vehicleAge']>;
  mileage: FormControl<IVehicle['mileage']>;
  numberOfDoors: FormControl<IVehicle['numberOfDoors']>;
  gearbox: FormControl<IVehicle['gearbox']>;
  color: FormControl<IVehicle['color']>;
  usage: FormControl<IVehicle['usage']>;
  isNew: FormControl<IVehicle['isNew']>;
  hasGarage: FormControl<IVehicle['hasGarage']>;
  hasParking: FormControl<IVehicle['hasParking']>;
  hasAlarmSystem: FormControl<IVehicle['hasAlarmSystem']>;
  hasSeatbeltAlarm: FormControl<IVehicle['hasSeatbeltAlarm']>;
  hasRearCamera: FormControl<IVehicle['hasRearCamera']>;
  hasRearRadar: FormControl<IVehicle['hasRearRadar']>;
  hasAbsSystem: FormControl<IVehicle['hasAbsSystem']>;
  hasGPS: FormControl<IVehicle['hasGPS']>;
  hasAirbag: FormControl<IVehicle['hasAirbag']>;
  navette: FormControl<IVehicle['navette']>;
};

export type VehicleFormGroup = FormGroup<VehicleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VehicleFormService {
  createVehicleFormGroup(vehicle: VehicleFormGroupInput = { id: null }): VehicleFormGroup {
    const vehicleRawValue = {
      ...this.getFormDefaults(),
      ...vehicle,
    };
    return new FormGroup<VehicleFormGroupContent>({
      id: new FormControl(
        { value: vehicleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      registrationNumber: new FormControl(vehicleRawValue.registrationNumber),
      registrationType: new FormControl(vehicleRawValue.registrationType),
      firstRegistrationDate: new FormControl(vehicleRawValue.firstRegistrationDate),
      technicalInspectionStatus: new FormControl(vehicleRawValue.technicalInspectionStatus),
      expirationDate: new FormControl(vehicleRawValue.expirationDate),
      newValue: new FormControl(vehicleRawValue.newValue),
      marketValue: new FormControl(vehicleRawValue.marketValue),
      brand: new FormControl(vehicleRawValue.brand),
      model: new FormControl(vehicleRawValue.model),
      fiscalPower: new FormControl(vehicleRawValue.fiscalPower),
      chassisNumber: new FormControl(vehicleRawValue.chassisNumber),
      energy: new FormControl(vehicleRawValue.energy),
      genre: new FormControl(vehicleRawValue.genre),
      nbrOfSeats: new FormControl(vehicleRawValue.nbrOfSeats),
      nbrOfStandingPlaces: new FormControl(vehicleRawValue.nbrOfStandingPlaces),
      emptyWeight: new FormControl(vehicleRawValue.emptyWeight),
      payload: new FormControl(vehicleRawValue.payload),
      bonusMalus: new FormControl(vehicleRawValue.bonusMalus),
      vehicleAge: new FormControl(vehicleRawValue.vehicleAge),
      mileage: new FormControl(vehicleRawValue.mileage),
      numberOfDoors: new FormControl(vehicleRawValue.numberOfDoors),
      gearbox: new FormControl(vehicleRawValue.gearbox),
      color: new FormControl(vehicleRawValue.color),
      usage: new FormControl(vehicleRawValue.usage),
      isNew: new FormControl(vehicleRawValue.isNew),
      hasGarage: new FormControl(vehicleRawValue.hasGarage),
      hasParking: new FormControl(vehicleRawValue.hasParking),
      hasAlarmSystem: new FormControl(vehicleRawValue.hasAlarmSystem),
      hasSeatbeltAlarm: new FormControl(vehicleRawValue.hasSeatbeltAlarm),
      hasRearCamera: new FormControl(vehicleRawValue.hasRearCamera),
      hasRearRadar: new FormControl(vehicleRawValue.hasRearRadar),
      hasAbsSystem: new FormControl(vehicleRawValue.hasAbsSystem),
      hasGPS: new FormControl(vehicleRawValue.hasGPS),
      hasAirbag: new FormControl(vehicleRawValue.hasAirbag),
      navette: new FormControl(vehicleRawValue.navette),
    });
  }

  getVehicle(form: VehicleFormGroup): IVehicle | NewVehicle {
    return form.getRawValue() as IVehicle | NewVehicle;
  }

  resetForm(form: VehicleFormGroup, vehicle: VehicleFormGroupInput): void {
    const vehicleRawValue = { ...this.getFormDefaults(), ...vehicle };
    form.reset(
      {
        ...vehicleRawValue,
        id: { value: vehicleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VehicleFormDefaults {
    return {
      id: null,
      isNew: false,
      hasGarage: false,
      hasParking: false,
      hasAlarmSystem: false,
      hasSeatbeltAlarm: false,
      hasRearCamera: false,
      hasRearRadar: false,
      hasAbsSystem: false,
      hasGPS: false,
      hasAirbag: false,
      navette: false,
    };
  }
}
