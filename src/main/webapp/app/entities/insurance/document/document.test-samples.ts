import dayjs from 'dayjs/esm';

import { IDocument, NewDocument } from './document.model';

export const sampleWithRequiredData: IDocument = {
  id: 'd73fe592-5ffb-420c-b189-0e268e08fa56',
};

export const sampleWithPartialData: IDocument = {
  id: 'a7631782-ef40-47fb-a0f8-2c21feb43606',
  docPath: 'upward',
  requestedDocType: 'VEHICLE_REGISTRATION',
  creationDate: dayjs('1976-06-01'),
};

export const sampleWithFullData: IDocument = {
  id: 'f0e5752e-2dfa-4fee-9c52-1c7118905599',
  name: 'an ugh our',
  docPath: 'per iterate',
  type: 'GENERATED_FILE',
  requestedDocType: 'VEHICLE_REGISTRATION',
  creationDate: dayjs('1976-06-01'),
};

export const sampleWithNewData: NewDocument = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
