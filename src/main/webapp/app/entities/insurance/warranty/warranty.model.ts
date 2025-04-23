import { IInsurancePack } from 'app/entities/insurance/insurance-pack/insurance-pack.model';

export interface IWarranty {
  id: string;
  name?: string | null;
  limit?: number | null;
  franchise?: number | null;
  price?: number | null;
  mandatory?: boolean | null;
  insurancePacks?: Pick<IInsurancePack, 'id'>[] | null;
}

export type NewWarranty = Omit<IWarranty, 'id'> & { id: null };
