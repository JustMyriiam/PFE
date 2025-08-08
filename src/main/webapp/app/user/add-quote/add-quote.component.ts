import { Component } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { FormlyFieldConfig, FormlyModule } from '@ngx-formly/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Step, StepList, StepPanel, StepPanels, Stepper } from 'primeng/stepper';
import { IClient, NewClient } from '../../entities/insurance/client/client.model';
import { IVehicle, NewVehicle } from '../../entities/insurance/vehicle/vehicle.model';
import { IInsurancePack } from '../../entities/insurance/insurance-pack/insurance-pack.model';
import { MaritalStatus } from '../../entities/enumerations/marital-status.model';
import { ProfessionalStatus } from '../../entities/enumerations/professional-status.model';
import { RegistrationType } from '../../entities/enumerations/registration-type.model';
import { Brand } from '../../entities/enumerations/brand.model';
import { TechnicalInspectionStatus } from '../../entities/enumerations/technical-inspection-status.model';
import { Gearbox } from '../../entities/enumerations/gearbox.model';
import { Energy } from '../../entities/enumerations/energy.model';
import { VehicleUsage } from '../../entities/enumerations/vehicle-usage.model';
import { InsurancePackName } from '../../entities/enumerations/insurance-pack-name.model';
import { NgClass, NgIf } from '@angular/common';

@Component({
  selector: 'jhi-add-quote',
  imports: [FaIconComponent, FormlyModule, FormsModule, Step, StepList, StepPanel, StepPanels, Stepper, ReactiveFormsModule, NgClass, NgIf],
  templateUrl: './add-quote.component.html',
  styleUrl: './add-quote.component.scss',
})
export class AddQuoteComponent {
  activeStep = 1;
  form: FormGroup = new FormGroup({
    client: new FormGroup({}),
    vehicle: new FormGroup({}),
    name: new FormControl(null),
  });

  protected readonly clientModel: NewClient = { id: null };
  protected readonly vehicleModel: NewVehicle = { id: null };
  protected readonly packModel: any = {};
  protected submittedData: { client: any; vehicle: any; pack: any } | undefined;

  fields: FormlyFieldConfig[] = [
    {
      key: 'client',
      fieldGroup: [
        {
          fieldGroupClassName: 'row',
          fieldGroup: [
            {
              className: 'col-6',
              key: 'lastName',
              type: 'input',
              templateOptions: {
                label: 'Last Name',
                required: false,
              },
            },
            {
              className: 'col-6',
              key: 'firstName',
              type: 'input',
              templateOptions: {
                label: 'First Name',
                required: false,
              },
            },
          ],
        },
        {
          fieldGroupClassName: 'row',
          fieldGroup: [
            {
              className: 'col-6',
              key: 'maritalStatus',
              type: 'select',
              templateOptions: {
                label: 'Marital Status',
                options: Object.keys(MaritalStatus).map(k => ({ label: k, value: k })),
              },
            },
            {
              className: 'col-6',
              key: 'clientAddress',
              type: 'select',
              templateOptions: {
                label: 'Adresse',
                options: [],
                valueProp: 'id',
                labelProp: 'id',
              },
            },
          ],
        },
        {
          fieldGroupClassName: 'row',
          fieldGroup: [
            {
              className: 'col-6',
              key: 'professionalStatus',
              type: 'select',
              templateOptions: {
                label: 'Professional Status',
                options: Object.keys(ProfessionalStatus).map(k => ({ label: k, value: k })),
              },
            },
            {
              className: 'col-6',
              key: 'jobTitle',
              type: 'input',
              templateOptions: {
                label: 'Job Title',
              },
            },
          ],
        },
        {
          fieldGroupClassName: 'row',
          fieldGroup: [
            {
              className: 'col-6',
              key: 'agency',
              type: 'input',
              templateOptions: {
                label: 'Agence',
              },
            },
          ],
        },
      ],
    },
  ];

  fieldsStep2: FormlyFieldConfig[] = [
    {
      key: 'vehicle',
      fieldGroup: [
        {
          fieldGroupClassName: 'row',
          fieldGroup: [
            {
              className: 'col-md-6',
              key: 'registrationNumber',
              type: 'input',
              props: {
                label: 'Matricule',
                placeholder: '123 TU 456',
                required: false,
              },
            },
            {
              className: 'col-md-6',
              key: 'registrationType',
              type: 'select',
              props: {
                label: "Type d'immatriculation",
                options: Object.keys(RegistrationType).map(k => ({ label: k, value: k })),
              },
            },
          ],
        },
        {
          fieldGroupClassName: 'row',
          fieldGroup: [
            {
              className: 'col-md-6',
              key: 'brand',
              type: 'select',
              props: {
                label: 'Marque',
                options: Object.keys(Brand).map(k => ({ label: k, value: k })),
                required: false,
              },
            },
            {
              className: 'col-md-6',
              key: 'model',
              type: 'input',
              props: {
                label: 'Modèle',
                placeholder: 'Clio, Corolla, etc.',
                required: false,
              },
            },
          ],
        },
        {
          fieldGroupClassName: 'row',
          fieldGroup: [
            {
              className: 'col-md-6',
              key: 'firstRegistrationDate',
              type: 'datepicker',
              props: {
                label: 'Date de première mise en circulation',
                required: false,
              },
            },
            {
              className: 'col-md-6',
              key: 'technicalInspectionStatus',
              type: 'select',
              props: {
                label: 'État de la visite technique',
                options: Object.keys(TechnicalInspectionStatus).map(k => ({ label: k, value: k })),
              },
            },
          ],
        },
        {
          fieldGroupClassName: 'row',
          fieldGroup: [
            {
              className: 'col-md-6',
              key: 'expirationDate',
              type: 'datepicker',
              props: {
                label: "Date d'expiration",
              },
            },
            {
              className: 'col-md-6',
              key: 'fiscalPower',
              type: 'input',
              props: {
                label: 'Puissance fiscale',
                type: 'number',
              },
            },
          ],
        },
        {
          fieldGroupClassName: 'row',
          fieldGroup: [
            {
              className: 'col-md-6',
              key: 'mileage',
              type: 'input',
              props: {
                label: 'Kilométrage',
                type: 'number',
                placeholder: 'ex: 120000',
              },
            },
            {
              className: 'col-md-6',
              key: 'color',
              type: 'input',
              props: {
                label: 'Couleur',
              },
            },
          ],
        },
        {
          fieldGroupClassName: 'row',
          fieldGroup: [
            {
              className: 'col-md-6',
              key: 'gearbox',
              type: 'select',
              props: {
                label: 'Boîte de vitesse',
                options: Object.keys(Gearbox).map(k => ({ label: k, value: k })),
              },
            },
            {
              className: 'col-md-6',
              key: 'energy',
              type: 'select',
              props: {
                label: 'Énergie',
                options: Object.keys(Energy).map(k => ({ label: k, value: k })),
              },
            },
          ],
        },
        {
          fieldGroupClassName: 'row',
          fieldGroup: [
            {
              className: 'col-md-6',
              key: 'usage',
              type: 'select',
              props: {
                label: 'Usage du véhicule',
                options: Object.keys(VehicleUsage).map(k => ({ label: k, value: k })),
              },
            },
            {
              className: 'col-md-6',
              key: 'isNew',
              type: 'checkbox',
              props: {
                label: 'Véhicule Neuf ?',
              },
            },
          ],
        },
      ],
    },
  ];

  pack: FormlyFieldConfig[] = [
    {
      key: 'name',
      type: 'radio',
      className: 'container',
      templateOptions: {
        label: 'Choisissez un Pack ',
        required: false,
        options: Object.keys(InsurancePackName).map(key => ({
          value: key,
          label: InsurancePackName[key as keyof typeof InsurancePackName],
        })),
      },
    },
  ];
  private http: any;

  confirmContract() {
    if (this.form.valid) {
      this.submittedData = {
        client: this.form.value.client,
        vehicle: this.form.value.vehicle,
        pack: this.form.value.name,
      };
      this.activeStep = 4;
    } else {
      alert('Veuillez remplir tous les champs requis.');
    }
  }

  getStepProgress(step: number): number {
    // Calculate progress based on filled fields for each step
    switch (step) {
      case 1:
        return this.calculateClientProgress();
      case 2:
        return this.calculateVehicleProgress();
      case 3:
        return this.form.value.name ? 100 : 0;
      case 4:
        return 100; // Final step is always 100% when reached
      default:
        return 0;
    }
  }

  private calculateClientProgress(): number {
    const clientData = this.form.value.client;
    if (!clientData) return 0;

    const allFields = ['lastName', 'firstName', 'maritalStatus', 'clientAddress', 'professionalStatus', 'jobTitle', 'agency'];

    let filledFields = 0;
    allFields.forEach(field => {
      if (clientData[field]) filledFields++;
    });

    return Math.round((filledFields / allFields.length) * 100);
  }

  private calculateVehicleProgress(): number {
    const vehicleData = this.form.value.vehicle;
    if (!vehicleData) return 0;

    const allFields = [
      'registrationNumber',
      'registrationType',
      'brand',
      'model',
      'firstRegistrationDate',
      'technicalInspectionStatus',
      'expirationDate',
      'fiscalPower',
      'mileage',
      'color',
      'gearbox',
      'energy',
      'usage',
      'isNew',
    ];

    let filledFields = 0;
    allFields.forEach(field => {
      if (vehicleData[field] !== null && vehicleData[field] !== undefined && vehicleData[field] !== '') {
        filledFields++;
      }
    });

    return Math.round((filledFields / allFields.length) * 100);
  }

  getCalculatedPrice() {
    // return this.http.get<{ totalPrice: string }>(
    //   'http://localhost:5678/webhook/c7fb5062-418c-4dea-92df-9d5e81695918'
    // );
    return '1500dtn';
  }
}
