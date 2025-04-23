import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IWarranty } from '../warranty.model';

@Component({
  selector: 'jhi-warranty-detail',
  templateUrl: './warranty-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class WarrantyDetailComponent {
  warranty = input<IWarranty | null>(null);

  previousState(): void {
    window.history.back();
  }
}
