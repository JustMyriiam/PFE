import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IWarranty } from 'app/entities/insurance/warranty/warranty.model';
import { WarrantyService } from 'app/entities/insurance/warranty/service/warranty.service';
import { IVehicle } from 'app/entities/insurance/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/insurance/vehicle/service/vehicle.service';
import { InsurancePackName } from 'app/entities/enumerations/insurance-pack-name.model';
import { InsurancePackService } from '../service/insurance-pack.service';
import { IInsurancePack } from '../insurance-pack.model';
import { InsurancePackFormGroup, InsurancePackFormService } from './insurance-pack-form.service';

@Component({
  selector: 'jhi-insurance-pack-update',
  templateUrl: './insurance-pack-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InsurancePackUpdateComponent implements OnInit {
  isSaving = false;
  insurancePack: IInsurancePack | null = null;
  insurancePackNameValues = Object.keys(InsurancePackName);

  warrantiesSharedCollection: IWarranty[] = [];
  vehiclesSharedCollection: IVehicle[] = [];

  protected insurancePackService = inject(InsurancePackService);
  protected insurancePackFormService = inject(InsurancePackFormService);
  protected warrantyService = inject(WarrantyService);
  protected vehicleService = inject(VehicleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InsurancePackFormGroup = this.insurancePackFormService.createInsurancePackFormGroup();

  compareWarranty = (o1: IWarranty | null, o2: IWarranty | null): boolean => this.warrantyService.compareWarranty(o1, o2);

  compareVehicle = (o1: IVehicle | null, o2: IVehicle | null): boolean => this.vehicleService.compareVehicle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ insurancePack }) => {
      this.insurancePack = insurancePack;
      if (insurancePack) {
        this.updateForm(insurancePack);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const insurancePack = this.insurancePackFormService.getInsurancePack(this.editForm);
    if (insurancePack.id !== null) {
      this.subscribeToSaveResponse(this.insurancePackService.update(insurancePack));
    } else {
      this.subscribeToSaveResponse(this.insurancePackService.create(insurancePack));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInsurancePack>>): void {
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

  protected updateForm(insurancePack: IInsurancePack): void {
    this.insurancePack = insurancePack;
    this.insurancePackFormService.resetForm(this.editForm, insurancePack);

    this.warrantiesSharedCollection = this.warrantyService.addWarrantyToCollectionIfMissing<IWarranty>(
      this.warrantiesSharedCollection,
      ...(insurancePack.warranties ?? []),
    );
    this.vehiclesSharedCollection = this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(
      this.vehiclesSharedCollection,
      insurancePack.vehicle,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.warrantyService
      .query()
      .pipe(map((res: HttpResponse<IWarranty[]>) => res.body ?? []))
      .pipe(
        map((warranties: IWarranty[]) =>
          this.warrantyService.addWarrantyToCollectionIfMissing<IWarranty>(warranties, ...(this.insurancePack?.warranties ?? [])),
        ),
      )
      .subscribe((warranties: IWarranty[]) => (this.warrantiesSharedCollection = warranties));

    this.vehicleService
      .query()
      .pipe(map((res: HttpResponse<IVehicle[]>) => res.body ?? []))
      .pipe(
        map((vehicles: IVehicle[]) => this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(vehicles, this.insurancePack?.vehicle)),
      )
      .subscribe((vehicles: IVehicle[]) => (this.vehiclesSharedCollection = vehicles));
  }
}
