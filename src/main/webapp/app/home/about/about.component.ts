import { Component } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

interface Benefit {
  title: string;
  description: string;
}

@Component({
  selector: 'jhi-about',
  templateUrl: './about.component.html',
  imports: [FaIconComponent],
  styleUrls: ['./about.component.scss'],
})
export class AboutComponent {}
