import { Component } from '@angular/core';
import { ClaimService } from '../../entities/insurance/claim/service/claim.service';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NewClaim } from '../../entities/insurance/claim/claim.model';
import { FormlyFieldConfig, FormlyModule } from '@ngx-formly/core';
import { FormlyBootstrapModule } from '@ngx-formly/bootstrap';

import dayjs from 'dayjs/esm';
import { NgIf } from '@angular/common';
import { ClaimStatus } from '../../entities/enumerations/claim-status.model';

@Component({
  selector: 'jhi-add-claim',
  standalone: true,
  imports: [ReactiveFormsModule, FormlyModule, NgIf, FormlyBootstrapModule],
  templateUrl: './add-claim.component.html',
  styleUrl: './add-claim.component.scss',
})
export class AddClaimComponent {
  form = new FormGroup({});
  model: NewClaim = {
    id: null,
    type: '',
    description: '',
    date: dayjs(),
    status: ClaimStatus.IN_PROGRESS,
    client: null,
    contract: null,
  };

  fields: FormlyFieldConfig[] = [
    {
      key: 'type',
      type: 'select',
      className: 'col-12 col-md-6', // Responsive column classes
      templateOptions: {
        label: 'Type de réclamation',
        required: true,
        options: [
          { value: 'CONTRACT_ISSUE', label: 'Problème de contrat' },
          { value: 'QUOTE_ISSUE', label: 'Problème de devis' },
          { value: 'OTHER', label: 'Autre' },
        ],
      },
    },
    {
      key: 'description',
      type: 'textarea',
      className: 'col-12', // Full width on all screens
      templateOptions: {
        label: 'Description',
        placeholder: 'Décrivez le problème rencontré',
        required: true,
        rows: 4, // Fewer rows on mobile
      },
    },
  ];

  alertMessage: string | null = null;
  alertType: 'success' | 'danger' | null = null;

  constructor(private claimService: ClaimService) {}

  submit(): void {
    if (this.form.valid) {
      this.claimService.create(this.model).subscribe({
        next: () => {
          this.alertType = 'success';
          this.alertMessage = 'Réclamation soumise avec succès !';
          this.form.reset();
          this.model = {
            id: null,
            date: dayjs(),
            type: '',
            description: '',
            status: ClaimStatus.IN_PROGRESS,
            client: null,
            contract: null,
          };
        },
        error: () => {
          this.alertType = 'danger';
          this.alertMessage = 'Une erreur est survenue lors de l’envoi.';
        },
      });
    } else {
      this.alertType = 'danger';
      this.alertMessage = 'Veuillez remplir tous les champs requis.';
    }
  }

  closeAlert(): void {
    this.alertMessage = null;
    this.alertType = null;
  }
}
