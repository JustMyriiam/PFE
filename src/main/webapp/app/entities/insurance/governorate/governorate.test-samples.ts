import { IGovernorate, NewGovernorate } from './governorate.model';

export const sampleWithRequiredData: IGovernorate = {
  id: 'b25a9c34-d65c-4838-863e-4a33caca39bf',
  name: 'configuration aha',
};

export const sampleWithPartialData: IGovernorate = {
  id: '327181bd-34f4-467a-8963-f51871d2e09d',
  name: 'beside peaceful',
};

export const sampleWithFullData: IGovernorate = {
  id: '35e17b7d-24ec-4065-af61-c0ae690d333a',
  name: 'pfft bashfully',
};

export const sampleWithNewData: NewGovernorate = {
  name: 'task upliftingly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
