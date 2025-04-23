import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInsurancePack, NewInsurancePack } from '../insurance-pack.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInsurancePack for edit and NewInsurancePackFormGroupInput for create.
 */
type InsurancePackFormGroupInput = IInsurancePack | PartialWithRequiredKeyOf<NewInsurancePack>;

type InsurancePackFormDefaults = Pick<NewInsurancePack, 'id' | 'warranties'>;

type InsurancePackFormGroupContent = {
  id: FormControl<IInsurancePack['id'] | NewInsurancePack['id']>;
  name: FormControl<IInsurancePack['name']>;
  desciption: FormControl<IInsurancePack['desciption']>;
  price: FormControl<IInsurancePack['price']>;
  warranties: FormControl<IInsurancePack['warranties']>;
  vehicle: FormControl<IInsurancePack['vehicle']>;
};

export type InsurancePackFormGroup = FormGroup<InsurancePackFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InsurancePackFormService {
  createInsurancePackFormGroup(insurancePack: InsurancePackFormGroupInput = { id: null }): InsurancePackFormGroup {
    const insurancePackRawValue = {
      ...this.getFormDefaults(),
      ...insurancePack,
    };
    return new FormGroup<InsurancePackFormGroupContent>({
      id: new FormControl(
        { value: insurancePackRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(insurancePackRawValue.name),
      desciption: new FormControl(insurancePackRawValue.desciption),
      price: new FormControl(insurancePackRawValue.price),
      warranties: new FormControl(insurancePackRawValue.warranties ?? []),
      vehicle: new FormControl(insurancePackRawValue.vehicle),
    });
  }

  getInsurancePack(form: InsurancePackFormGroup): IInsurancePack | NewInsurancePack {
    return form.getRawValue() as IInsurancePack | NewInsurancePack;
  }

  resetForm(form: InsurancePackFormGroup, insurancePack: InsurancePackFormGroupInput): void {
    const insurancePackRawValue = { ...this.getFormDefaults(), ...insurancePack };
    form.reset(
      {
        ...insurancePackRawValue,
        id: { value: insurancePackRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InsurancePackFormDefaults {
    return {
      id: null,
      warranties: [],
    };
  }
}
