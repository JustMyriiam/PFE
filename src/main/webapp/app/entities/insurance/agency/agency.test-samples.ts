import { IAgency, NewAgency } from './agency.model';

export const sampleWithRequiredData: IAgency = {
  id: 'fabeae80-5d83-48f6-b524-347f5ea4e6f7',
};

export const sampleWithPartialData: IAgency = {
  id: 'ebb95d5c-a98a-4b5f-a342-73486c8e8cd7',
  name: 'lashes hepatitis',
  address: 'easily linseed interestingly',
  region: 'where towards scruple',
};

export const sampleWithFullData: IAgency = {
  id: 'f280d89b-b7a8-4422-a546-0ce7c1154092',
  name: 'indeed fly',
  address: 'fax nor',
  region: 'innocently internal if',
  phoneNumber: 'starch in times',
  managerName: 'beyond duh biodegradable',
};

export const sampleWithNewData: NewAgency = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
