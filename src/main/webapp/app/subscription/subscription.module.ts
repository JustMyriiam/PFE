import { NgModule } from '@angular/core';
import { CommonModule, JsonPipe } from '@angular/common';
import { Step, StepList, Stepper } from 'primeng/stepper';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@NgModule({
  declarations: [],
  imports: [CommonModule, JsonPipe, Stepper, StepList, Step, FormsModule, FontAwesomeModule],
})
export class SubscriptionModule {}
