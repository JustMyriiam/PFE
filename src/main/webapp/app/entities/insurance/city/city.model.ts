import { IGovernorate } from 'app/entities/insurance/governorate/governorate.model';

export interface ICity {
  id: string;
  name?: string | null;
  postalCode?: string | null;
  governorate?: Pick<IGovernorate, 'id'> | null;
}

export type NewCity = Omit<ICity, 'id'> & { id: null };
