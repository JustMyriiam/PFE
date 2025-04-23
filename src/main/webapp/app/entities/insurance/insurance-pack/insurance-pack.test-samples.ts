import { IInsurancePack, NewInsurancePack } from './insurance-pack.model';

export const sampleWithRequiredData: IInsurancePack = {
  id: 'cbe2dde4-7fa1-456d-87fb-3ee49fbf3b2a',
};

export const sampleWithPartialData: IInsurancePack = {
  id: '61f98bbb-4fe2-4bf2-8a73-0e72752df5d7',
  desciption: 'configuration',
};

export const sampleWithFullData: IInsurancePack = {
  id: '18d93edb-0229-48bb-8268-557babbe7d77',
  name: 'BASIC',
  desciption: 'starboard overconfidently whether',
  price: 11043.07,
};

export const sampleWithNewData: NewInsurancePack = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
