import { ICity } from 'app/entities/insurance/city/city.model';

export interface IClientAddress {
  id: string;
  address?: string | null;
  region?: string | null;
  city?: Pick<ICity, 'id'> | null;
}

export type NewClientAddress = Omit<IClientAddress, 'id'> & { id: null };
