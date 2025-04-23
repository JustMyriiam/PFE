import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IClientAddress, NewClientAddress } from '../client-address.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClientAddress for edit and NewClientAddressFormGroupInput for create.
 */
type ClientAddressFormGroupInput = IClientAddress | PartialWithRequiredKeyOf<NewClientAddress>;

type ClientAddressFormDefaults = Pick<NewClientAddress, 'id'>;

type ClientAddressFormGroupContent = {
  id: FormControl<IClientAddress['id'] | NewClientAddress['id']>;
  address: FormControl<IClientAddress['address']>;
  region: FormControl<IClientAddress['region']>;
  city: FormControl<IClientAddress['city']>;
};

export type ClientAddressFormGroup = FormGroup<ClientAddressFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClientAddressFormService {
  createClientAddressFormGroup(clientAddress: ClientAddressFormGroupInput = { id: null }): ClientAddressFormGroup {
    const clientAddressRawValue = {
      ...this.getFormDefaults(),
      ...clientAddress,
    };
    return new FormGroup<ClientAddressFormGroupContent>({
      id: new FormControl(
        { value: clientAddressRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      address: new FormControl(clientAddressRawValue.address),
      region: new FormControl(clientAddressRawValue.region),
      city: new FormControl(clientAddressRawValue.city),
    });
  }

  getClientAddress(form: ClientAddressFormGroup): IClientAddress | NewClientAddress {
    return form.getRawValue() as IClientAddress | NewClientAddress;
  }

  resetForm(form: ClientAddressFormGroup, clientAddress: ClientAddressFormGroupInput): void {
    const clientAddressRawValue = { ...this.getFormDefaults(), ...clientAddress };
    form.reset(
      {
        ...clientAddressRawValue,
        id: { value: clientAddressRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClientAddressFormDefaults {
    return {
      id: null,
    };
  }
}
