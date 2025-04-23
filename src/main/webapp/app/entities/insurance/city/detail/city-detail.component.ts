import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICity } from '../city.model';

@Component({
  selector: 'jhi-city-detail',
  templateUrl: './city-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CityDetailComponent {
  city = input<ICity | null>(null);

  previousState(): void {
    window.history.back();
  }
}
