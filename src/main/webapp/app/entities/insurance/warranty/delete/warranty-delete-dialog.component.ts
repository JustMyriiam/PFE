import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWarranty } from '../warranty.model';
import { WarrantyService } from '../service/warranty.service';

@Component({
  templateUrl: './warranty-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WarrantyDeleteDialogComponent {
  warranty?: IWarranty;

  protected warrantyService = inject(WarrantyService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.warrantyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
