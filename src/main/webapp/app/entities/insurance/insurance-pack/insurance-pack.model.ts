import { IWarranty } from 'app/entities/insurance/warranty/warranty.model';
import { IVehicle } from 'app/entities/insurance/vehicle/vehicle.model';
import { InsurancePackName } from 'app/entities/enumerations/insurance-pack-name.model';

export interface IInsurancePack {
  id: string;
  name?: keyof typeof InsurancePackName | null;
  desciption?: string | null;
  price?: number | null;
  warranties?: Pick<IWarranty, 'id'>[] | null;
  vehicle?: Pick<IVehicle, 'id'> | null;
}

export type NewInsurancePack = Omit<IInsurancePack, 'id'> & { id: null };
