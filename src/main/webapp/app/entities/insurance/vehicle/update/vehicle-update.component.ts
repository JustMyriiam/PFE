import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { RegistrationType } from 'app/entities/enumerations/registration-type.model';
import { TechnicalInspectionStatus } from 'app/entities/enumerations/technical-inspection-status.model';
import { Brand } from 'app/entities/enumerations/brand.model';
import { Energy } from 'app/entities/enumerations/energy.model';
import { Gearbox } from 'app/entities/enumerations/gearbox.model';
import { VehicleUsage } from 'app/entities/enumerations/vehicle-usage.model';
import { VehicleService } from '../service/vehicle.service';
import { IVehicle } from '../vehicle.model';
import { VehicleFormGroup, VehicleFormService } from './vehicle-form.service';

@Component({
  selector: 'jhi-vehicle-update',
  templateUrl: './vehicle-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VehicleUpdateComponent implements OnInit {
  isSaving = false;
  vehicle: IVehicle | null = null;
  registrationTypeValues = Object.keys(RegistrationType);
  technicalInspectionStatusValues = Object.keys(TechnicalInspectionStatus);
  brandValues = Object.keys(Brand);
  energyValues = Object.keys(Energy);
  gearboxValues = Object.keys(Gearbox);
  vehicleUsageValues = Object.keys(VehicleUsage);

  protected vehicleService = inject(VehicleService);
  protected vehicleFormService = inject(VehicleFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VehicleFormGroup = this.vehicleFormService.createVehicleFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicle }) => {
      this.vehicle = vehicle;
      if (vehicle) {
        this.updateForm(vehicle);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicle = this.vehicleFormService.getVehicle(this.editForm);
    if (vehicle.id !== null) {
      this.subscribeToSaveResponse(this.vehicleService.update(vehicle));
    } else {
      this.subscribeToSaveResponse(this.vehicleService.create(vehicle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicle>>): void {
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

  protected updateForm(vehicle: IVehicle): void {
    this.vehicle = vehicle;
    this.vehicleFormService.resetForm(this.editForm, vehicle);
  }
}
