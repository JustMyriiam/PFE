import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInsurancePack } from 'app/entities/insurance/insurance-pack/insurance-pack.model';
import { InsurancePackService } from 'app/entities/insurance/insurance-pack/service/insurance-pack.service';
import { IWarranty } from '../warranty.model';
import { WarrantyService } from '../service/warranty.service';
import { WarrantyFormGroup, WarrantyFormService } from './warranty-form.service';

@Component({
  selector: 'jhi-warranty-update',
  templateUrl: './warranty-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WarrantyUpdateComponent implements OnInit {
  isSaving = false;
  warranty: IWarranty | null = null;

  insurancePacksSharedCollection: IInsurancePack[] = [];

  protected warrantyService = inject(WarrantyService);
  protected warrantyFormService = inject(WarrantyFormService);
  protected insurancePackService = inject(InsurancePackService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WarrantyFormGroup = this.warrantyFormService.createWarrantyFormGroup();

  compareInsurancePack = (o1: IInsurancePack | null, o2: IInsurancePack | null): boolean =>
    this.insurancePackService.compareInsurancePack(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ warranty }) => {
      this.warranty = warranty;
      if (warranty) {
        this.updateForm(warranty);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const warranty = this.warrantyFormService.getWarranty(this.editForm);
    if (warranty.id !== null) {
      this.subscribeToSaveResponse(this.warrantyService.update(warranty));
    } else {
      this.subscribeToSaveResponse(this.warrantyService.create(warranty));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWarranty>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(warranty: IWarranty): void {
    this.warranty = warranty;
    this.warrantyFormService.resetForm(this.editForm, warranty);

    this.insurancePacksSharedCollection = this.insurancePackService.addInsurancePackToCollectionIfMissing<IInsurancePack>(
      this.insurancePacksSharedCollection,
      ...(warranty.insurancePacks ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.insurancePackService
      .query()
      .pipe(map((res: HttpResponse<IInsurancePack[]>) => res.body ?? []))
      .pipe(
        map((insurancePacks: IInsurancePack[]) =>
          this.insurancePackService.addInsurancePackToCollectionIfMissing<IInsurancePack>(
            insurancePacks,
            ...(this.warranty?.insurancePacks ?? []),
          ),
        ),
      )
      .subscribe((insurancePacks: IInsurancePack[]) => (this.insurancePacksSharedCollection = insurancePacks));
  }
}
