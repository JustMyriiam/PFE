import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IQuote, NewQuote } from '../quote.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuote for edit and NewQuoteFormGroupInput for create.
 */
type QuoteFormGroupInput = IQuote | PartialWithRequiredKeyOf<NewQuote>;

type QuoteFormDefaults = Pick<NewQuote, 'id'>;

type QuoteFormGroupContent = {
  id: FormControl<IQuote['id'] | NewQuote['id']>;
  date: FormControl<IQuote['date']>;
  estimatedAmount: FormControl<IQuote['estimatedAmount']>;
  vehicle: FormControl<IQuote['vehicle']>;
  client: FormControl<IQuote['client']>;
};

export type QuoteFormGroup = FormGroup<QuoteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuoteFormService {
  createQuoteFormGroup(quote: QuoteFormGroupInput = { id: null }): QuoteFormGroup {
    const quoteRawValue = {
      ...this.getFormDefaults(),
      ...quote,
    };
    return new FormGroup<QuoteFormGroupContent>({
      id: new FormControl(
        { value: quoteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(quoteRawValue.date),
      estimatedAmount: new FormControl(quoteRawValue.estimatedAmount),
      vehicle: new FormControl(quoteRawValue.vehicle),
      client: new FormControl(quoteRawValue.client),
    });
  }

  getQuote(form: QuoteFormGroup): IQuote | NewQuote {
    return form.getRawValue() as IQuote | NewQuote;
  }

  resetForm(form: QuoteFormGroup, quote: QuoteFormGroupInput): void {
    const quoteRawValue = { ...this.getFormDefaults(), ...quote };
    form.reset(
      {
        ...quoteRawValue,
        id: { value: quoteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QuoteFormDefaults {
    return {
      id: null,
    };
  }
}
