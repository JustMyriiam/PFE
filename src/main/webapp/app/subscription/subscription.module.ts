import { NgModule } from '@angular/core';
import { CommonModule, JsonPipe } from '@angular/common';
import { Step, StepList, Stepper } from 'primeng/stepper';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { FormlyModule } from '@ngx-formly/core';
import { FormlyBootstrapModule } from '@ngx-formly/bootstrap';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    JsonPipe,
    Stepper,
    StepList,
    Step,
    FormsModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    FormlyBootstrapModule,
    FormlyModule.forRoot(),
  ],
})
export class SubscriptionModule {}
