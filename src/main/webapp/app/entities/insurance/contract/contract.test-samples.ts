import dayjs from 'dayjs/esm';

import { IContract, NewContract } from './contract.model';

export const sampleWithRequiredData: IContract = {
  id: '0a125206-f922-43a9-a0af-20b2bad88a4a',
};

export const sampleWithPartialData: IContract = {
  id: '530ced0f-7fc4-4051-98b0-de908f8821ac',
  startDate: dayjs('1976-06-01'),
  upfrontPremium: 9610.52,
  cost: 3742.95,
  taxes: 15200.85,
  fSSR: 18644.99,
  fPAC: 1984.61,
  contractType: 'RENEWABLE',
  paymentPlan: 'ANNUAL',
};

export const sampleWithFullData: IContract = {
  id: '4e904eaa-70c0-4893-9b1e-69f7db8c43b7',
  contractNumber: 'operating alert tabulate',
  duration: 'golden ouch where',
  startDate: dayjs('1976-05-31'),
  endDate: dayjs('1976-06-01'),
  netPremium: 7307.86,
  upfrontPremium: 23451.44,
  cost: 14386.41,
  taxes: 18333.9,
  fSSR: 18229.78,
  fPAC: 1736.67,
  tFGA: 26444.14,
  contractType: 'CLOSED',
  paymentPlan: 'ANNUAL',
};

export const sampleWithNewData: NewContract = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
