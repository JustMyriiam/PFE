import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IClaim } from '../claim.model';
import { ClaimService } from '../service/claim.service';

@Component({
  templateUrl: './claim-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ClaimDeleteDialogComponent {
  claim?: IClaim;

  protected claimService = inject(ClaimService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.claimService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
