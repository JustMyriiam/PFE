import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IGovernorate } from '../governorate.model';
import { GovernorateService } from '../service/governorate.service';

@Component({
  templateUrl: './governorate-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class GovernorateDeleteDialogComponent {
  governorate?: IGovernorate;

  protected governorateService = inject(GovernorateService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.governorateService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
