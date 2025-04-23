import dayjs from 'dayjs/esm';
import { IContract } from 'app/entities/insurance/contract/contract.model';
import { DocType } from 'app/entities/enumerations/doc-type.model';
import { RequestedDocType } from 'app/entities/enumerations/requested-doc-type.model';

export interface IDocument {
  id: string;
  name?: string | null;
  docPath?: string | null;
  type?: keyof typeof DocType | null;
  requestedDocType?: keyof typeof RequestedDocType | null;
  creationDate?: dayjs.Dayjs | null;
  contract?: Pick<IContract, 'id'> | null;
}

export type NewDocument = Omit<IDocument, 'id'> & { id: null };
