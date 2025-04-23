import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IClaim, NewClaim } from '../claim.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClaim for edit and NewClaimFormGroupInput for create.
 */
type ClaimFormGroupInput = IClaim | PartialWithRequiredKeyOf<NewClaim>;

type ClaimFormDefaults = Pick<NewClaim, 'id'>;

type ClaimFormGroupContent = {
  id: FormControl<IClaim['id'] | NewClaim['id']>;
  type: FormControl<IClaim['type']>;
  description: FormControl<IClaim['description']>;
  date: FormControl<IClaim['date']>;
  status: FormControl<IClaim['status']>;
  client: FormControl<IClaim['client']>;
  contract: FormControl<IClaim['contract']>;
};

export type ClaimFormGroup = FormGroup<ClaimFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClaimFormService {
  createClaimFormGroup(claim: ClaimFormGroupInput = { id: null }): ClaimFormGroup {
    const claimRawValue = {
      ...this.getFormDefaults(),
      ...claim,
    };
    return new FormGroup<ClaimFormGroupContent>({
      id: new FormControl(
        { value: claimRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(claimRawValue.type),
      description: new FormControl(claimRawValue.description),
      date: new FormControl(claimRawValue.date),
      status: new FormControl(claimRawValue.status),
      client: new FormControl(claimRawValue.client),
      contract: new FormControl(claimRawValue.contract),
    });
  }

  getClaim(form: ClaimFormGroup): IClaim | NewClaim {
    return form.getRawValue() as IClaim | NewClaim;
  }

  resetForm(form: ClaimFormGroup, claim: ClaimFormGroupInput): void {
    const claimRawValue = { ...this.getFormDefaults(), ...claim };
    form.reset(
      {
        ...claimRawValue,
        id: { value: claimRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClaimFormDefaults {
    return {
      id: null,
    };
  }
}
