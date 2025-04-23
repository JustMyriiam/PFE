import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IClient } from 'app/entities/insurance/client/client.model';
import { ClientService } from 'app/entities/insurance/client/service/client.service';
import { IContract } from 'app/entities/insurance/contract/contract.model';
import { ContractService } from 'app/entities/insurance/contract/service/contract.service';
import { ClaimStatus } from 'app/entities/enumerations/claim-status.model';
import { ClaimService } from '../service/claim.service';
import { IClaim } from '../claim.model';
import { ClaimFormGroup, ClaimFormService } from './claim-form.service';

@Component({
  selector: 'jhi-claim-update',
  templateUrl: './claim-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClaimUpdateComponent implements OnInit {
  isSaving = false;
  claim: IClaim | null = null;
  claimStatusValues = Object.keys(ClaimStatus);

  clientsSharedCollection: IClient[] = [];
  contractsSharedCollection: IContract[] = [];

  protected claimService = inject(ClaimService);
  protected claimFormService = inject(ClaimFormService);
  protected clientService = inject(ClientService);
  protected contractService = inject(ContractService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ClaimFormGroup = this.claimFormService.createClaimFormGroup();

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ claim }) => {
      this.claim = claim;
      if (claim) {
        this.updateForm(claim);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const claim = this.claimFormService.getClaim(this.editForm);
    if (claim.id !== null) {
      this.subscribeToSaveResponse(this.claimService.update(claim));
    } else {
      this.subscribeToSaveResponse(this.claimService.create(claim));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClaim>>): void {
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

  protected updateForm(claim: IClaim): void {
    this.claim = claim;
    this.claimFormService.resetForm(this.editForm, claim);

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing<IClient>(this.clientsSharedCollection, claim.client);
    this.contractsSharedCollection = this.contractService.addContractToCollectionIfMissing<IContract>(
      this.contractsSharedCollection,
      claim.contract,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.claim?.client)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));

    this.contractService
      .query()
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) => this.contractService.addContractToCollectionIfMissing<IContract>(contracts, this.claim?.contract)),
      )
      .subscribe((contracts: IContract[]) => (this.contractsSharedCollection = contracts));
  }
}
