import dayjs from 'dayjs/esm';

import { IQuote, NewQuote } from './quote.model';

export const sampleWithRequiredData: IQuote = {
  id: 'bb06dc47-9fcb-4b27-aec5-bc94152e6369',
};

export const sampleWithPartialData: IQuote = {
  id: '48ebbcf7-dd2e-4c73-82e0-3ffd76717999',
};

export const sampleWithFullData: IQuote = {
  id: 'eac726f2-c066-43c5-b072-b0254dd1c42c',
  date: dayjs('1976-06-01'),
  estimatedAmount: 19665.34,
};

export const sampleWithNewData: NewQuote = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
