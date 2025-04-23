import { ICity } from 'app/entities/insurance/city/city.model';

export interface IAgency {
  id: string;
  name?: string | null;
  address?: string | null;
  region?: string | null;
  phoneNumber?: string | null;
  managerName?: string | null;
  city?: Pick<ICity, 'id'> | null;
}

export type NewAgency = Omit<IAgency, 'id'> & { id: null };
