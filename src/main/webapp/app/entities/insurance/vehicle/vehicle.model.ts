import dayjs from 'dayjs/esm';
import { RegistrationType } from 'app/entities/enumerations/registration-type.model';
import { TechnicalInspectionStatus } from 'app/entities/enumerations/technical-inspection-status.model';
import { Brand } from 'app/entities/enumerations/brand.model';
import { Energy } from 'app/entities/enumerations/energy.model';
import { Gearbox } from 'app/entities/enumerations/gearbox.model';
import { VehicleUsage } from 'app/entities/enumerations/vehicle-usage.model';

export interface IVehicle {
  id: string;
  registrationNumber?: string | null;
  registrationType?: keyof typeof RegistrationType | null;
  firstRegistrationDate?: dayjs.Dayjs | null;
  technicalInspectionStatus?: keyof typeof TechnicalInspectionStatus | null;
  expirationDate?: dayjs.Dayjs | null;
  newValue?: number | null;
  marketValue?: number | null;
  brand?: keyof typeof Brand | null;
  model?: string | null;
  fiscalPower?: number | null;
  chassisNumber?: string | null;
  energy?: keyof typeof Energy | null;
  genre?: string | null;
  nbrOfSeats?: number | null;
  nbrOfStandingPlaces?: number | null;
  emptyWeight?: number | null;
  payload?: number | null;
  bonusMalus?: number | null;
  vehicleAge?: string | null;
  mileage?: number | null;
  numberOfDoors?: number | null;
  gearbox?: keyof typeof Gearbox | null;
  color?: string | null;
  usage?: keyof typeof VehicleUsage | null;
  isNew?: boolean | null;
  hasGarage?: boolean | null;
  hasParking?: boolean | null;
  hasAlarmSystem?: boolean | null;
  hasSeatbeltAlarm?: boolean | null;
  hasRearCamera?: boolean | null;
  hasRearRadar?: boolean | null;
  hasAbsSystem?: boolean | null;
  hasGPS?: boolean | null;
  hasAirbag?: boolean | null;
  navette?: boolean | null;
}

export type NewVehicle = Omit<IVehicle, 'id'> & { id: null };
