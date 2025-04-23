import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IGovernorate, NewGovernorate } from '../governorate.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGovernorate for edit and NewGovernorateFormGroupInput for create.
 */
type GovernorateFormGroupInput = IGovernorate | PartialWithRequiredKeyOf<NewGovernorate>;

type GovernorateFormDefaults = Pick<NewGovernorate, 'id'>;

type GovernorateFormGroupContent = {
  id: FormControl<IGovernorate['id'] | NewGovernorate['id']>;
  name: FormControl<IGovernorate['name']>;
};

export type GovernorateFormGroup = FormGroup<GovernorateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GovernorateFormService {
  createGovernorateFormGroup(governorate: GovernorateFormGroupInput = { id: null }): GovernorateFormGroup {
    const governorateRawValue = {
      ...this.getFormDefaults(),
      ...governorate,
    };
    return new FormGroup<GovernorateFormGroupContent>({
      id: new FormControl(
        { value: governorateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(governorateRawValue.name, {
        validators: [Validators.required],
      }),
    });
  }

  getGovernorate(form: GovernorateFormGroup): IGovernorate | NewGovernorate {
    return form.getRawValue() as IGovernorate | NewGovernorate;
  }

  resetForm(form: GovernorateFormGroup, governorate: GovernorateFormGroupInput): void {
    const governorateRawValue = { ...this.getFormDefaults(), ...governorate };
    form.reset(
      {
        ...governorateRawValue,
        id: { value: governorateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): GovernorateFormDefaults {
    return {
      id: null,
    };
  }
}
