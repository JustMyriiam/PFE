import dayjs from 'dayjs/esm';

import { IClaim, NewClaim } from './claim.model';

export const sampleWithRequiredData: IClaim = {
  id: '51613e94-9ade-4ad8-a9d5-c19082ea0f0f',
};

export const sampleWithPartialData: IClaim = {
  id: 'ea556a7e-f656-4076-aab8-783933a12a74',
  type: 'openly slope',
  status: 'IN_PROGRESS',
};

export const sampleWithFullData: IClaim = {
  id: '424928ed-8c11-4ae9-bcfe-e99cbb97cab1',
  type: 'for formation',
  description: 'any',
  date: dayjs('1976-06-01'),
  status: 'RESOLVED',
};

export const sampleWithNewData: NewClaim = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
