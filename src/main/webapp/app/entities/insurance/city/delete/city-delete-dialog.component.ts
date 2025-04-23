import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICity } from '../city.model';
import { CityService } from '../service/city.service';

@Component({
  templateUrl: './city-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CityDeleteDialogComponent {
  city?: ICity;

  protected cityService = inject(CityService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.cityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
