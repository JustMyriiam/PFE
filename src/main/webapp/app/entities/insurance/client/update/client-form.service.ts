import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IClient, NewClient } from '../client.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClient for edit and NewClientFormGroupInput for create.
 */
type ClientFormGroupInput = IClient | PartialWithRequiredKeyOf<NewClient>;

type ClientFormDefaults = Pick<NewClient, 'id'>;

type ClientFormGroupContent = {
  id: FormControl<IClient['id'] | NewClient['id']>;
  lastName: FormControl<IClient['lastName']>;
  firstName: FormControl<IClient['firstName']>;
  identityType: FormControl<IClient['identityType']>;
  identityNumber: FormControl<IClient['identityNumber']>;
  identityEmissionDate: FormControl<IClient['identityEmissionDate']>;
  birthDate: FormControl<IClient['birthDate']>;
  birthPlace: FormControl<IClient['birthPlace']>;
  identityIssueDate: FormControl<IClient['identityIssueDate']>;
  identityPlaceOfIssue: FormControl<IClient['identityPlaceOfIssue']>;
  maritalStatus: FormControl<IClient['maritalStatus']>;
  nbrOfchildren: FormControl<IClient['nbrOfchildren']>;
  professionalEmail: FormControl<IClient['professionalEmail']>;
  personalEmail: FormControl<IClient['personalEmail']>;
  primaryPhoneNumber: FormControl<IClient['primaryPhoneNumber']>;
  secondaryPhoneNumber: FormControl<IClient['secondaryPhoneNumber']>;
  faxNumber: FormControl<IClient['faxNumber']>;
  nationality: FormControl<IClient['nationality']>;
  gender: FormControl<IClient['gender']>;
  jobTitle: FormControl<IClient['jobTitle']>;
  professionalStatus: FormControl<IClient['professionalStatus']>;
  bank: FormControl<IClient['bank']>;
  agency: FormControl<IClient['agency']>;
  rib: FormControl<IClient['rib']>;
  drivingLicenseNumber: FormControl<IClient['drivingLicenseNumber']>;
  drivingLicenseIssueDate: FormControl<IClient['drivingLicenseIssueDate']>;
  drivingLicenseCategory: FormControl<IClient['drivingLicenseCategory']>;
  user: FormControl<IClient['user']>;
  clientAddress: FormControl<IClient['clientAddress']>;
};

export type ClientFormGroup = FormGroup<ClientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClientFormService {
  createClientFormGroup(client: ClientFormGroupInput = { id: null }): ClientFormGroup {
    const clientRawValue = {
      ...this.getFormDefaults(),
      ...client,
    };
    return new FormGroup<ClientFormGroupContent>({
      id: new FormControl(
        { value: clientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      lastName: new FormControl(clientRawValue.lastName),
      firstName: new FormControl(clientRawValue.firstName),
      identityType: new FormControl(clientRawValue.identityType),
      identityNumber: new FormControl(clientRawValue.identityNumber),
      identityEmissionDate: new FormControl(clientRawValue.identityEmissionDate),
      birthDate: new FormControl(clientRawValue.birthDate),
      birthPlace: new FormControl(clientRawValue.birthPlace),
      identityIssueDate: new FormControl(clientRawValue.identityIssueDate),
      identityPlaceOfIssue: new FormControl(clientRawValue.identityPlaceOfIssue),
      maritalStatus: new FormControl(clientRawValue.maritalStatus),
      nbrOfchildren: new FormControl(clientRawValue.nbrOfchildren),
      professionalEmail: new FormControl(clientRawValue.professionalEmail),
      personalEmail: new FormControl(clientRawValue.personalEmail),
      primaryPhoneNumber: new FormControl(clientRawValue.primaryPhoneNumber),
      secondaryPhoneNumber: new FormControl(clientRawValue.secondaryPhoneNumber),
      faxNumber: new FormControl(clientRawValue.faxNumber),
      nationality: new FormControl(clientRawValue.nationality),
      gender: new FormControl(clientRawValue.gender),
      jobTitle: new FormControl(clientRawValue.jobTitle),
      professionalStatus: new FormControl(clientRawValue.professionalStatus),
      bank: new FormControl(clientRawValue.bank),
      agency: new FormControl(clientRawValue.agency),
      rib: new FormControl(clientRawValue.rib),
      drivingLicenseNumber: new FormControl(clientRawValue.drivingLicenseNumber),
      drivingLicenseIssueDate: new FormControl(clientRawValue.drivingLicenseIssueDate),
      drivingLicenseCategory: new FormControl(clientRawValue.drivingLicenseCategory),
      user: new FormControl(clientRawValue.user),
      clientAddress: new FormControl(clientRawValue.clientAddress),
    });
  }

  getClient(form: ClientFormGroup): IClient | NewClient {
    return form.getRawValue() as IClient | NewClient;
  }

  resetForm(form: ClientFormGroup, client: ClientFormGroupInput): void {
    const clientRawValue = { ...this.getFormDefaults(), ...client };
    form.reset(
      {
        ...clientRawValue,
        id: { value: clientRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClientFormDefaults {
    return {
      id: null,
    };
  }
}
