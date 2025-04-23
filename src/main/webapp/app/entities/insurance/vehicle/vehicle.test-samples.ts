import dayjs from 'dayjs/esm';

import { IVehicle, NewVehicle } from './vehicle.model';

export const sampleWithRequiredData: IVehicle = {
  id: '34b267ff-dd72-4ec7-b3e5-19600675da53',
};

export const sampleWithPartialData: IVehicle = {
  id: 'ceb32ecf-05cd-4256-b4c5-e96dd55beb2b',
  registrationType: 'ADMINISTRATIVE_AND_TECHNICAL_STAFF',
  firstRegistrationDate: dayjs('1976-06-01'),
  marketValue: 5242.74,
  brand: 'BMW',
  fiscalPower: 21388,
  energy: 'DIESEL',
  nbrOfSeats: 2906,
  nbrOfStandingPlaces: 2253,
  emptyWeight: 1931,
  mileage: 10695,
  gearbox: 'AUTOMATIC',
  isNew: false,
  hasParking: true,
  hasRearRadar: false,
  hasAbsSystem: false,
  hasGPS: false,
  hasAirbag: false,
};

export const sampleWithFullData: IVehicle = {
  id: '6ccc43ae-95f0-4742-817b-a6e81007eb46',
  registrationNumber: 'deploy',
  registrationType: 'CONSULAR_CORPS',
  firstRegistrationDate: dayjs('1976-05-31'),
  technicalInspectionStatus: 'VALID',
  expirationDate: dayjs('1976-05-31'),
  newValue: 25790.82,
  marketValue: 3109.11,
  brand: 'MAHINDRA',
  model: 'extroverted gadzooks astride',
  fiscalPower: 28162,
  chassisNumber: 'tune',
  energy: 'DIESEL',
  genre: 'oh pro',
  nbrOfSeats: 26837,
  nbrOfStandingPlaces: 16636,
  emptyWeight: 16368,
  payload: 19981,
  bonusMalus: 10380,
  vehicleAge: 'celsius ecstatic',
  mileage: 18733,
  numberOfDoors: 10139,
  gearbox: 'AUTOMATIC',
  color: 'lime',
  usage: 'PERSONAL',
  isNew: false,
  hasGarage: false,
  hasParking: false,
  hasAlarmSystem: true,
  hasSeatbeltAlarm: true,
  hasRearCamera: false,
  hasRearRadar: true,
  hasAbsSystem: false,
  hasGPS: true,
  hasAirbag: true,
  navette: false,
};

export const sampleWithNewData: NewVehicle = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
