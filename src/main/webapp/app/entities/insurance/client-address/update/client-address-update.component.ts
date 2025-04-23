import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICity } from 'app/entities/insurance/city/city.model';
import { CityService } from 'app/entities/insurance/city/service/city.service';
import { IClientAddress } from '../client-address.model';
import { ClientAddressService } from '../service/client-address.service';
import { ClientAddressFormGroup, ClientAddressFormService } from './client-address-form.service';

@Component({
  selector: 'jhi-client-address-update',
  templateUrl: './client-address-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClientAddressUpdateComponent implements OnInit {
  isSaving = false;
  clientAddress: IClientAddress | null = null;

  citiesSharedCollection: ICity[] = [];

  protected clientAddressService = inject(ClientAddressService);
  protected clientAddressFormService = inject(ClientAddressFormService);
  protected cityService = inject(CityService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ClientAddressFormGroup = this.clientAddressFormService.createClientAddressFormGroup();

  compareCity = (o1: ICity | null, o2: ICity | null): boolean => this.cityService.compareCity(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clientAddress }) => {
      this.clientAddress = clientAddress;
      if (clientAddress) {
        this.updateForm(clientAddress);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clientAddress = this.clientAddressFormService.getClientAddress(this.editForm);
    if (clientAddress.id !== null) {
      this.subscribeToSaveResponse(this.clientAddressService.update(clientAddress));
    } else {
      this.subscribeToSaveResponse(this.clientAddressService.create(clientAddress));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClientAddress>>): void {
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

  protected updateForm(clientAddress: IClientAddress): void {
    this.clientAddress = clientAddress;
    this.clientAddressFormService.resetForm(this.editForm, clientAddress);

    this.citiesSharedCollection = this.cityService.addCityToCollectionIfMissing<ICity>(this.citiesSharedCollection, clientAddress.city);
  }

  protected loadRelationshipsOptions(): void {
    this.cityService
      .query()
      .pipe(map((res: HttpResponse<ICity[]>) => res.body ?? []))
      .pipe(map((cities: ICity[]) => this.cityService.addCityToCollectionIfMissing<ICity>(cities, this.clientAddress?.city)))
      .subscribe((cities: ICity[]) => (this.citiesSharedCollection = cities));
  }
}
