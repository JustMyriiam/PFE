import { ICity, NewCity } from './city.model';

export const sampleWithRequiredData: ICity = {
  id: '188033eb-db10-47e0-870a-76fda694e54e',
};

export const sampleWithPartialData: ICity = {
  id: '6914fc0f-ca6e-4d73-a3ac-e0743023390b',
  postalCode: 'offset',
};

export const sampleWithFullData: ICity = {
  id: 'b9718de9-4788-4ffb-8e27-23761a1b58e4',
  name: 'whoever seal',
  postalCode: 'because woot phooey',
};

export const sampleWithNewData: NewCity = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
