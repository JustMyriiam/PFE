import dayjs from 'dayjs/esm';
import { IVehicle } from 'app/entities/insurance/vehicle/vehicle.model';
import { IClient } from 'app/entities/insurance/client/client.model';

export interface IQuote {
  id: string;
  date?: dayjs.Dayjs | null;
  estimatedAmount?: number | null;
  vehicle?: Pick<IVehicle, 'id'> | null;
  client?: Pick<IClient, 'id'> | null;
}

export type NewQuote = Omit<IQuote, 'id'> & { id: null };
