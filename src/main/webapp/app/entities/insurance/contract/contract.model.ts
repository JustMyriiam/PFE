import dayjs from 'dayjs/esm';
import { IVehicle } from 'app/entities/insurance/vehicle/vehicle.model';
import { IClient } from 'app/entities/insurance/client/client.model';
import { IAgency } from 'app/entities/insurance/agency/agency.model';
import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { PaymentPlan } from 'app/entities/enumerations/payment-plan.model';

export interface IContract {
  id: string;
  contractNumber?: string | null;
  duration?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  netPremium?: number | null;
  upfrontPremium?: number | null;
  cost?: number | null;
  taxes?: number | null;
  fSSR?: number | null;
  fPAC?: number | null;
  tFGA?: number | null;
  contractType?: keyof typeof ContractType | null;
  paymentPlan?: keyof typeof PaymentPlan | null;
  vehicle?: Pick<IVehicle, 'id'> | null;
  client?: Pick<IClient, 'id'> | null;
  agency?: Pick<IAgency, 'id'> | null;
}

export type NewContract = Omit<IContract, 'id'> & { id: null };
