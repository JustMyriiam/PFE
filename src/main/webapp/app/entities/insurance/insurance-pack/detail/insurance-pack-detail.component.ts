import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IInsurancePack } from '../insurance-pack.model';

@Component({
  selector: 'jhi-insurance-pack-detail',
  templateUrl: './insurance-pack-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class InsurancePackDetailComponent {
  insurancePack = input<IInsurancePack | null>(null);

  previousState(): void {
    window.history.back();
  }
}
