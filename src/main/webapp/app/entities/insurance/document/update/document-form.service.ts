import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDocument, NewDocument } from '../document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocument for edit and NewDocumentFormGroupInput for create.
 */
type DocumentFormGroupInput = IDocument | PartialWithRequiredKeyOf<NewDocument>;

type DocumentFormDefaults = Pick<NewDocument, 'id'>;

type DocumentFormGroupContent = {
  id: FormControl<IDocument['id'] | NewDocument['id']>;
  name: FormControl<IDocument['name']>;
  docPath: FormControl<IDocument['docPath']>;
  type: FormControl<IDocument['type']>;
  requestedDocType: FormControl<IDocument['requestedDocType']>;
  creationDate: FormControl<IDocument['creationDate']>;
  contract: FormControl<IDocument['contract']>;
};

export type DocumentFormGroup = FormGroup<DocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentFormService {
  createDocumentFormGroup(document: DocumentFormGroupInput = { id: null }): DocumentFormGroup {
    const documentRawValue = {
      ...this.getFormDefaults(),
      ...document,
    };
    return new FormGroup<DocumentFormGroupContent>({
      id: new FormControl(
        { value: documentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(documentRawValue.name),
      docPath: new FormControl(documentRawValue.docPath),
      type: new FormControl(documentRawValue.type),
      requestedDocType: new FormControl(documentRawValue.requestedDocType),
      creationDate: new FormControl(documentRawValue.creationDate),
      contract: new FormControl(documentRawValue.contract),
    });
  }

  getDocument(form: DocumentFormGroup): IDocument | NewDocument {
    return form.getRawValue() as IDocument | NewDocument;
  }

  resetForm(form: DocumentFormGroup, document: DocumentFormGroupInput): void {
    const documentRawValue = { ...this.getFormDefaults(), ...document };
    form.reset(
      {
        ...documentRawValue,
        id: { value: documentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentFormDefaults {
    return {
      id: null,
    };
  }
}
