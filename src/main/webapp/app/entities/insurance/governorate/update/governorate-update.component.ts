import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IGovernorate } from '../governorate.model';
import { GovernorateService } from '../service/governorate.service';
import { GovernorateFormGroup, GovernorateFormService } from './governorate-form.service';

@Component({
  selector: 'jhi-governorate-update',
  templateUrl: './governorate-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GovernorateUpdateComponent implements OnInit {
  isSaving = false;
  governorate: IGovernorate | null = null;

  protected governorateService = inject(GovernorateService);
  protected governorateFormService = inject(GovernorateFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GovernorateFormGroup = this.governorateFormService.createGovernorateFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ governorate }) => {
      this.governorate = governorate;
      if (governorate) {
        this.updateForm(governorate);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const governorate = this.governorateFormService.getGovernorate(this.editForm);
    if (governorate.id !== null) {
      this.subscribeToSaveResponse(this.governorateService.update(governorate));
    } else {
      this.subscribeToSaveResponse(this.governorateService.create(governorate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGovernorate>>): void {
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

  protected updateForm(governorate: IGovernorate): void {
    this.governorate = governorate;
    this.governorateFormService.resetForm(this.editForm, governorate);
  }
}
