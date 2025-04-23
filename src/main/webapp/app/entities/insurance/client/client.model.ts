import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IClientAddress } from 'app/entities/insurance/client-address/client-address.model';
import { IdentityType } from 'app/entities/enumerations/identity-type.model';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { ProfessionalStatus } from 'app/entities/enumerations/professional-status.model';

export interface IClient {
  id: string;
  lastName?: string | null;
  firstName?: string | null;
  identityType?: keyof typeof IdentityType | null;
  identityNumber?: string | null;
  identityEmissionDate?: dayjs.Dayjs | null;
  birthDate?: dayjs.Dayjs | null;
  birthPlace?: string | null;
  identityIssueDate?: dayjs.Dayjs | null;
  identityPlaceOfIssue?: string | null;
  maritalStatus?: keyof typeof MaritalStatus | null;
  nbrOfchildren?: number | null;
  professionalEmail?: string | null;
  personalEmail?: string | null;
  primaryPhoneNumber?: string | null;
  secondaryPhoneNumber?: string | null;
  faxNumber?: string | null;
  nationality?: string | null;
  gender?: keyof typeof Gender | null;
  jobTitle?: string | null;
  professionalStatus?: keyof typeof ProfessionalStatus | null;
  bank?: string | null;
  agency?: string | null;
  rib?: string | null;
  drivingLicenseNumber?: string | null;
  drivingLicenseIssueDate?: dayjs.Dayjs | null;
  drivingLicenseCategory?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  clientAddress?: Pick<IClientAddress, 'id'> | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
