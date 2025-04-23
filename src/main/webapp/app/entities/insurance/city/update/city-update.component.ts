import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IGovernorate } from 'app/entities/insurance/governorate/governorate.model';
import { GovernorateService } from 'app/entities/insurance/governorate/service/governorate.service';
import { ICity } from '../city.model';
import { CityService } from '../service/city.service';
import { CityFormGroup, CityFormService } from './city-form.service';

@Component({
  selector: 'jhi-city-update',
  templateUrl: './city-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CityUpdateComponent implements OnInit {
  isSaving = false;
  city: ICity | null = null;

  governoratesSharedCollection: IGovernorate[] = [];

  protected cityService = inject(CityService);
  protected cityFormService = inject(CityFormService);
  protected governorateService = inject(GovernorateService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CityFormGroup = this.cityFormService.createCityFormGroup();

  compareGovernorate = (o1: IGovernorate | null, o2: IGovernorate | null): boolean => this.governorateService.compareGovernorate(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ city }) => {
      this.city = city;
      if (city) {
        this.updateForm(city);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const city = this.cityFormService.getCity(this.editForm);
    if (city.id !== null) {
      this.subscribeToSaveResponse(this.cityService.update(city));
    } else {
      this.subscribeToSaveResponse(this.cityService.create(city));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICity>>): void {
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

  protected updateForm(city: ICity): void {
    this.city = city;
    this.cityFormService.resetForm(this.editForm, city);

    this.governoratesSharedCollection = this.governorateService.addGovernorateToCollectionIfMissing<IGovernorate>(
      this.governoratesSharedCollection,
      city.governorate,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.governorateService
      .query()
      .pipe(map((res: HttpResponse<IGovernorate[]>) => res.body ?? []))
      .pipe(
        map((governorates: IGovernorate[]) =>
          this.governorateService.addGovernorateToCollectionIfMissing<IGovernorate>(governorates, this.city?.governorate),
        ),
      )
      .subscribe((governorates: IGovernorate[]) => (this.governoratesSharedCollection = governorates));
  }
}
