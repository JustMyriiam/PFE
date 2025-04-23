import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAgency, NewAgency } from '../agency.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAgency for edit and NewAgencyFormGroupInput for create.
 */
type AgencyFormGroupInput = IAgency | PartialWithRequiredKeyOf<NewAgency>;

type AgencyFormDefaults = Pick<NewAgency, 'id'>;

type AgencyFormGroupContent = {
  id: FormControl<IAgency['id'] | NewAgency['id']>;
  name: FormControl<IAgency['name']>;
  address: FormControl<IAgency['address']>;
  region: FormControl<IAgency['region']>;
  phoneNumber: FormControl<IAgency['phoneNumber']>;
  managerName: FormControl<IAgency['managerName']>;
  city: FormControl<IAgency['city']>;
};

export type AgencyFormGroup = FormGroup<AgencyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AgencyFormService {
  createAgencyFormGroup(agency: AgencyFormGroupInput = { id: null }): AgencyFormGroup {
    const agencyRawValue = {
      ...this.getFormDefaults(),
      ...agency,
    };
    return new FormGroup<AgencyFormGroupContent>({
      id: new FormControl(
        { value: agencyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(agencyRawValue.name),
      address: new FormControl(agencyRawValue.address),
      region: new FormControl(agencyRawValue.region),
      phoneNumber: new FormControl(agencyRawValue.phoneNumber),
      managerName: new FormControl(agencyRawValue.managerName),
      city: new FormControl(agencyRawValue.city),
    });
  }

  getAgency(form: AgencyFormGroup): IAgency | NewAgency {
    return form.getRawValue() as IAgency | NewAgency;
  }

  resetForm(form: AgencyFormGroup, agency: AgencyFormGroupInput): void {
    const agencyRawValue = { ...this.getFormDefaults(), ...agency };
    form.reset(
      {
        ...agencyRawValue,
        id: { value: agencyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AgencyFormDefaults {
    return {
      id: null,
    };
  }
}
