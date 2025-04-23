import dayjs from 'dayjs/esm';

import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: '7d28d909-c61e-4284-a2e7-f5439e53da45',
};

export const sampleWithPartialData: IClient = {
  id: 'f6509bab-7237-48c6-9543-a0446f80f6c4',
  lastName: 'Gulgowski',
  firstName: 'Abner',
  identityType: 'CIN',
  identityNumber: 'which',
  identityEmissionDate: dayjs('1976-06-01'),
  birthDate: dayjs('1976-06-01'),
  identityIssueDate: dayjs('1976-06-01'),
  identityPlaceOfIssue: 'extremely until out',
  professionalEmail: 'gymnast',
  personalEmail: 'boo misspend meanwhile',
  faxNumber: 'rightfully unnaturally yet',
  jobTitle: 'Internal Branding Supervisor',
  professionalStatus: 'EMPLOYEE',
  agency: 'meanwhile furthermore',
  rib: 'uh-huh on',
  drivingLicenseNumber: 'until',
  drivingLicenseIssueDate: dayjs('1976-06-01'),
  drivingLicenseCategory: 'or silently trick',
};

export const sampleWithFullData: IClient = {
  id: 'ff15c7e3-2cf0-47c8-ac48-a89dc8e5f529',
  lastName: 'Greenfelder',
  firstName: 'Loma',
  identityType: 'PASSPORT',
  identityNumber: 'unexpectedly',
  identityEmissionDate: dayjs('1976-06-01'),
  birthDate: dayjs('1976-06-01'),
  birthPlace: 'quiet',
  identityIssueDate: dayjs('1976-06-01'),
  identityPlaceOfIssue: 'yak beyond cheerfully',
  maritalStatus: 'DIVORCED',
  nbrOfchildren: 31152,
  professionalEmail: 'during forearm',
  personalEmail: 'nougat silky sundae',
  primaryPhoneNumber: 'ack pace crossly',
  secondaryPhoneNumber: 'pressure whoever',
  faxNumber: 'viability',
  nationality: 'after',
  gender: 'MALE',
  jobTitle: 'Human Accounts Technician',
  professionalStatus: 'EMPLOYEE',
  bank: 'down after',
  agency: 'but upwardly',
  rib: 'boohoo the',
  drivingLicenseNumber: 'cow ew',
  drivingLicenseIssueDate: dayjs('1976-06-01'),
  drivingLicenseCategory: 'pretty',
};

export const sampleWithNewData: NewClient = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
