import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICity } from 'app/entities/insurance/city/city.model';
import { CityService } from 'app/entities/insurance/city/service/city.service';
import { IAgency } from '../agency.model';
import { AgencyService } from '../service/agency.service';
import { AgencyFormGroup, AgencyFormService } from './agency-form.service';

@Component({
  selector: 'jhi-agency-update',
  templateUrl: './agency-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AgencyUpdateComponent implements OnInit {
  isSaving = false;
  agency: IAgency | null = null;

  citiesSharedCollection: ICity[] = [];

  protected agencyService = inject(AgencyService);
  protected agencyFormService = inject(AgencyFormService);
  protected cityService = inject(CityService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AgencyFormGroup = this.agencyFormService.createAgencyFormGroup();

  compareCity = (o1: ICity | null, o2: ICity | null): boolean => this.cityService.compareCity(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agency }) => {
      this.agency = agency;
      if (agency) {
        this.updateForm(agency);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agency = this.agencyFormService.getAgency(this.editForm);
    if (agency.id !== null) {
      this.subscribeToSaveResponse(this.agencyService.update(agency));
    } else {
      this.subscribeToSaveResponse(this.agencyService.create(agency));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgency>>): void {
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

  protected updateForm(agency: IAgency): void {
    this.agency = agency;
    this.agencyFormService.resetForm(this.editForm, agency);

    this.citiesSharedCollection = this.cityService.addCityToCollectionIfMissing<ICity>(this.citiesSharedCollection, agency.city);
  }

  protected loadRelationshipsOptions(): void {
    this.cityService
      .query()
      .pipe(map((res: HttpResponse<ICity[]>) => res.body ?? []))
      .pipe(map((cities: ICity[]) => this.cityService.addCityToCollectionIfMissing<ICity>(cities, this.agency?.city)))
      .subscribe((cities: ICity[]) => (this.citiesSharedCollection = cities));
  }
}
