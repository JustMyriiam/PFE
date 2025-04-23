import { IWarranty, NewWarranty } from './warranty.model';

export const sampleWithRequiredData: IWarranty = {
  id: '179605d3-32b8-4a97-ac1f-cb8680251799',
};

export const sampleWithPartialData: IWarranty = {
  id: '50edb18c-3013-41c2-aa82-7e7476a76229',
  name: 'spook',
  limit: 2490.38,
  franchise: 10575.98,
  mandatory: true,
};

export const sampleWithFullData: IWarranty = {
  id: '1b43d1a7-dd31-4b94-ba81-594de8f26f6b',
  name: 'finally hope',
  limit: 19803.28,
  franchise: 23618.14,
  price: 16489.73,
  mandatory: false,
};

export const sampleWithNewData: NewWarranty = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
