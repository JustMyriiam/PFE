import { IClientAddress, NewClientAddress } from './client-address.model';

export const sampleWithRequiredData: IClientAddress = {
  id: 'fe169760-0827-4a25-ad79-76c8a346dd5a',
};

export const sampleWithPartialData: IClientAddress = {
  id: '1a2c2fea-08d6-4f8f-b6d6-b51b4476f1a6',
  address: 'ditch evenly ack',
};

export const sampleWithFullData: IClientAddress = {
  id: '8c9f151f-6525-46b3-ad89-0c37ec5b226e',
  address: 'hm gerbil waist',
  region: 'jut repurpose gripper',
};

export const sampleWithNewData: NewClientAddress = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
