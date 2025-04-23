import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInsurancePack } from '../insurance-pack.model';
import { InsurancePackService } from '../service/insurance-pack.service';

@Component({
  templateUrl: './insurance-pack-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InsurancePackDeleteDialogComponent {
  insurancePack?: IInsurancePack;

  protected insurancePackService = inject(InsurancePackService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.insurancePackService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
