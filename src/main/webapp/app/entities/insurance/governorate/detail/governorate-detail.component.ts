import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IGovernorate } from '../governorate.model';

@Component({
  selector: 'jhi-governorate-detail',
  templateUrl: './governorate-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class GovernorateDetailComponent {
  governorate = input<IGovernorate | null>(null);

  previousState(): void {
    window.history.back();
  }
}
