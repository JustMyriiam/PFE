import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVehicle } from 'app/entities/insurance/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/insurance/vehicle/service/vehicle.service';
import { IClient } from 'app/entities/insurance/client/client.model';
import { ClientService } from 'app/entities/insurance/client/service/client.service';
import { IAgency } from 'app/entities/insurance/agency/agency.model';
import { AgencyService } from 'app/entities/insurance/agency/service/agency.service';
import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { PaymentPlan } from 'app/entities/enumerations/payment-plan.model';
import { ContractService } from '../service/contract.service';
import { IContract } from '../contract.model';
import { ContractFormGroup, ContractFormService } from './contract-form.service';

@Component({
  selector: 'jhi-contract-update',
  templateUrl: './contract-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContractUpdateComponent implements OnInit {
  isSaving = false;
  contract: IContract | null = null;
  contractTypeValues = Object.keys(ContractType);
  paymentPlanValues = Object.keys(PaymentPlan);

  vehiclesCollection: IVehicle[] = [];
  clientsSharedCollection: IClient[] = [];
  agenciesSharedCollection: IAgency[] = [];

  protected contractService = inject(ContractService);
  protected contractFormService = inject(ContractFormService);
  protected vehicleService = inject(VehicleService);
  protected clientService = inject(ClientService);
  protected agencyService = inject(AgencyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContractFormGroup = this.contractFormService.createContractFormGroup();

  compareVehicle = (o1: IVehicle | null, o2: IVehicle | null): boolean => this.vehicleService.compareVehicle(o1, o2);

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  compareAgency = (o1: IAgency | null, o2: IAgency | null): boolean => this.agencyService.compareAgency(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contract }) => {
      this.contract = contract;
      if (contract) {
        this.updateForm(contract);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contract = this.contractFormService.getContract(this.editForm);
    if (contract.id !== null) {
      this.subscribeToSaveResponse(this.contractService.update(contract));
    } else {
      this.subscribeToSaveResponse(this.contractService.create(contract));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContract>>): void {
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

  protected updateForm(contract: IContract): void {
    this.contract = contract;
    this.contractFormService.resetForm(this.editForm, contract);

    this.vehiclesCollection = this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(this.vehiclesCollection, contract.vehicle);
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing<IClient>(
      this.clientsSharedCollection,
      contract.client,
    );
    this.agenciesSharedCollection = this.agencyService.addAgencyToCollectionIfMissing<IAgency>(
      this.agenciesSharedCollection,
      contract.agency,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vehicleService
      .query({ 'contractId.specified': 'false' })
      .pipe(map((res: HttpResponse<IVehicle[]>) => res.body ?? []))
      .pipe(map((vehicles: IVehicle[]) => this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(vehicles, this.contract?.vehicle)))
      .subscribe((vehicles: IVehicle[]) => (this.vehiclesCollection = vehicles));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.contract?.client)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));

    this.agencyService
      .query()
      .pipe(map((res: HttpResponse<IAgency[]>) => res.body ?? []))
      .pipe(map((agencies: IAgency[]) => this.agencyService.addAgencyToCollectionIfMissing<IAgency>(agencies, this.contract?.agency)))
      .subscribe((agencies: IAgency[]) => (this.agenciesSharedCollection = agencies));
  }
}
