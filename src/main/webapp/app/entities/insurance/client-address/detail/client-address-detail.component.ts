import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IClientAddress } from '../client-address.model';

@Component({
  selector: 'jhi-client-address-detail',
  templateUrl: './client-address-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ClientAddressDetailComponent {
  clientAddress = input<IClientAddress | null>(null);

  previousState(): void {
    window.history.back();
  }
}
