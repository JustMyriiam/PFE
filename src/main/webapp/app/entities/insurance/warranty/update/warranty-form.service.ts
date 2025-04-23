import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IWarranty, NewWarranty } from '../warranty.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWarranty for edit and NewWarrantyFormGroupInput for create.
 */
type WarrantyFormGroupInput = IWarranty | PartialWithRequiredKeyOf<NewWarranty>;

type WarrantyFormDefaults = Pick<NewWarranty, 'id' | 'mandatory' | 'insurancePacks'>;

type WarrantyFormGroupContent = {
  id: FormControl<IWarranty['id'] | NewWarranty['id']>;
  name: FormControl<IWarranty['name']>;
  limit: FormControl<IWarranty['limit']>;
  franchise: FormControl<IWarranty['franchise']>;
  price: FormControl<IWarranty['price']>;
  mandatory: FormControl<IWarranty['mandatory']>;
  insurancePacks: FormControl<IWarranty['insurancePacks']>;
};

export type WarrantyFormGroup = FormGroup<WarrantyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WarrantyFormService {
  createWarrantyFormGroup(warranty: WarrantyFormGroupInput = { id: null }): WarrantyFormGroup {
    const warrantyRawValue = {
      ...this.getFormDefaults(),
      ...warranty,
    };
    return new FormGroup<WarrantyFormGroupContent>({
      id: new FormControl(
        { value: warrantyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(warrantyRawValue.name),
      limit: new FormControl(warrantyRawValue.limit),
      franchise: new FormControl(warrantyRawValue.franchise),
      price: new FormControl(warrantyRawValue.price),
      mandatory: new FormControl(warrantyRawValue.mandatory),
      insurancePacks: new FormControl(warrantyRawValue.insurancePacks ?? []),
    });
  }

  getWarranty(form: WarrantyFormGroup): IWarranty | NewWarranty {
    return form.getRawValue() as IWarranty | NewWarranty;
  }

  resetForm(form: WarrantyFormGroup, warranty: WarrantyFormGroupInput): void {
    const warrantyRawValue = { ...this.getFormDefaults(), ...warranty };
    form.reset(
      {
        ...warrantyRawValue,
        id: { value: warrantyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WarrantyFormDefaults {
    return {
      id: null,
      mandatory: false,
      insurancePacks: [],
    };
  }
}
