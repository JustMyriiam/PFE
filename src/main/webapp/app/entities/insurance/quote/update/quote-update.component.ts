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
import { QuoteService } from '../service/quote.service';
import { IQuote } from '../quote.model';
import { QuoteFormGroup, QuoteFormService } from './quote-form.service';

@Component({
  selector: 'jhi-quote-update',
  templateUrl: './quote-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuoteUpdateComponent implements OnInit {
  isSaving = false;
  quote: IQuote | null = null;

  vehiclesCollection: IVehicle[] = [];
  clientsSharedCollection: IClient[] = [];

  protected quoteService = inject(QuoteService);
  protected quoteFormService = inject(QuoteFormService);
  protected vehicleService = inject(VehicleService);
  protected clientService = inject(ClientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuoteFormGroup = this.quoteFormService.createQuoteFormGroup();

  compareVehicle = (o1: IVehicle | null, o2: IVehicle | null): boolean => this.vehicleService.compareVehicle(o1, o2);

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quote }) => {
      this.quote = quote;
      if (quote) {
        this.updateForm(quote);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quote = this.quoteFormService.getQuote(this.editForm);
    if (quote.id !== null) {
      this.subscribeToSaveResponse(this.quoteService.update(quote));
    } else {
      this.subscribeToSaveResponse(this.quoteService.create(quote));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuote>>): void {
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

  protected updateForm(quote: IQuote): void {
    this.quote = quote;
    this.quoteFormService.resetForm(this.editForm, quote);

    this.vehiclesCollection = this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(this.vehiclesCollection, quote.vehicle);
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing<IClient>(this.clientsSharedCollection, quote.client);
  }

  protected loadRelationshipsOptions(): void {
    this.vehicleService
      .query({ 'quoteId.specified': 'false' })
      .pipe(map((res: HttpResponse<IVehicle[]>) => res.body ?? []))
      .pipe(map((vehicles: IVehicle[]) => this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(vehicles, this.quote?.vehicle)))
      .subscribe((vehicles: IVehicle[]) => (this.vehiclesCollection = vehicles));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.quote?.client)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }
}
