import dayjs from 'dayjs/esm';
import { IClient } from 'app/entities/insurance/client/client.model';
import { IContract } from 'app/entities/insurance/contract/contract.model';
import { ClaimStatus } from 'app/entities/enumerations/claim-status.model';

export interface IClaim {
  id: string;
  type?: string | null;
  description?: string | null;
  date?: dayjs.Dayjs | null;
  status?: keyof typeof ClaimStatus | null;
  client?: Pick<IClient, 'id'> | null;
  contract?: Pick<IContract, 'id'> | null;
}

export type NewClaim = Omit<IClaim, 'id'> & { id: null };
