import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IClaim } from '../claim.model';

@Component({
  selector: 'jhi-claim-detail',
  templateUrl: './claim-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class ClaimDetailComponent {
  claim = input<IClaim | null>(null);

  previousState(): void {
    window.history.back();
  }
}
