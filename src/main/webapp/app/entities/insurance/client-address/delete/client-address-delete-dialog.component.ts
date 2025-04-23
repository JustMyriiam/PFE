import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IClientAddress } from '../client-address.model';
import { ClientAddressService } from '../service/client-address.service';

@Component({
  templateUrl: './client-address-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ClientAddressDeleteDialogComponent {
  clientAddress?: IClientAddress;

  protected clientAddressService = inject(ClientAddressService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.clientAddressService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
