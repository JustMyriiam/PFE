import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IContract, NewContract } from '../contract.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContract for edit and NewContractFormGroupInput for create.
 */
type ContractFormGroupInput = IContract | PartialWithRequiredKeyOf<NewContract>;

type ContractFormDefaults = Pick<NewContract, 'id'>;

type ContractFormGroupContent = {
  id: FormControl<IContract['id'] | NewContract['id']>;
  contractNumber: FormControl<IContract['contractNumber']>;
  duration: FormControl<IContract['duration']>;
  startDate: FormControl<IContract['startDate']>;
  endDate: FormControl<IContract['endDate']>;
  netPremium: FormControl<IContract['netPremium']>;
  upfrontPremium: FormControl<IContract['upfrontPremium']>;
  cost: FormControl<IContract['cost']>;
  taxes: FormControl<IContract['taxes']>;
  fSSR: FormControl<IContract['fSSR']>;
  fPAC: FormControl<IContract['fPAC']>;
  tFGA: FormControl<IContract['tFGA']>;
  contractType: FormControl<IContract['contractType']>;
  paymentPlan: FormControl<IContract['paymentPlan']>;
  vehicle: FormControl<IContract['vehicle']>;
  client: FormControl<IContract['client']>;
  agency: FormControl<IContract['agency']>;
};

export type ContractFormGroup = FormGroup<ContractFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContractFormService {
  createContractFormGroup(contract: ContractFormGroupInput = { id: null }): ContractFormGroup {
    const contractRawValue = {
      ...this.getFormDefaults(),
      ...contract,
    };
    return new FormGroup<ContractFormGroupContent>({
      id: new FormControl(
        { value: contractRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      contractNumber: new FormControl(contractRawValue.contractNumber),
      duration: new FormControl(contractRawValue.duration),
      startDate: new FormControl(contractRawValue.startDate),
      endDate: new FormControl(contractRawValue.endDate),
      netPremium: new FormControl(contractRawValue.netPremium),
      upfrontPremium: new FormControl(contractRawValue.upfrontPremium),
      cost: new FormControl(contractRawValue.cost),
      taxes: new FormControl(contractRawValue.taxes),
      fSSR: new FormControl(contractRawValue.fSSR),
      fPAC: new FormControl(contractRawValue.fPAC),
      tFGA: new FormControl(contractRawValue.tFGA),
      contractType: new FormControl(contractRawValue.contractType),
      paymentPlan: new FormControl(contractRawValue.paymentPlan),
      vehicle: new FormControl(contractRawValue.vehicle),
      client: new FormControl(contractRawValue.client),
      agency: new FormControl(contractRawValue.agency),
    });
  }

  getContract(form: ContractFormGroup): IContract | NewContract {
    return form.getRawValue() as IContract | NewContract;
  }

  resetForm(form: ContractFormGroup, contract: ContractFormGroupInput): void {
    const contractRawValue = { ...this.getFormDefaults(), ...contract };
    form.reset(
      {
        ...contractRawValue,
        id: { value: contractRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContractFormDefaults {
    return {
      id: null,
    };
  }
}
