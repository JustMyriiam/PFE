import { Component, model } from '@angular/core';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormlyModule } from '@ngx-formly/core';
import { FormlyBootstrapModule } from '@ngx-formly/bootstrap';
import { NgClass } from '@angular/common';
import { Step, StepList, StepPanel, StepPanels, Stepper } from 'primeng/stepper';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { IClient } from '../../entities/insurance/client/client.model';
import { IdentityType } from '../../entities/enumerations/identity-type.model';
import { MaritalStatus } from '../../entities/enumerations/marital-status.model';
import { Gender } from '../../entities/enumerations/gender.model';
import { ProfessionalStatus } from '../../entities/enumerations/professional-status.model';
import { VehicleUsage } from '../../entities/enumerations/vehicle-usage.model';
import { Energy } from '../../entities/enumerations/energy.model';
import { Gearbox } from '../../entities/enumerations/gearbox.model';
import { TechnicalInspectionStatus } from '../../entities/enumerations/technical-inspection-status.model';
import { Brand } from '../../entities/enumerations/brand.model';
import { RegistrationType } from '../../entities/enumerations/registration-type.model';
import { IVehicle } from '../../entities/insurance/vehicle/vehicle.model';
import { IWarranty } from '../../entities/insurance/warranty/warranty.model';
import { IInsurancePack } from '../../entities/insurance/insurance-pack/insurance-pack.model';
import { InsurancePackName } from '../../entities/enumerations/insurance-pack-name.model';

@Component({
  selector: 'jhi-stepper-1',
  imports: [
    FormlyModule,
    StepList,
    Stepper,
    Step,
    StepPanels,
    StepPanel,
    NgClass,
    FaIconComponent,
    ReactiveFormsModule,
    FormlyBootstrapModule,
  ],
  templateUrl: './stepper-1.component.html',
  styleUrl: './stepper-1.component.scss',
})
export class Stepper1Component {
  novalidate: any;
  activeStep = 1;
  form = new FormGroup({});
  protected readonly clientModel: IClient = { id: '' };
  protected readonly vehicleModel: IVehicle = { id: '' };
  protected readonly insurancePackModel: IInsurancePack[] = [];

  fields: FormlyFieldConfig[] = [
    {
      fieldGroupClassName: 'row',
      fieldGroup: [
        {
          className: 'col-6',
          key: 'lastName',
          type: 'input',
          templateOptions: {
            label: 'Last Name',
            required: true,
          },
        },
        {
          className: 'col-6',
          key: 'firstName',
          type: 'input',
          templateOptions: {
            label: 'First Name',
            required: true,
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
  ];

  fieldsStep2: FormlyFieldConfig[] = [
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
            required: true,
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
            required: true,
          },
        },
        {
          className: 'col-md-6',
          key: 'model',
          type: 'input',
          props: {
            label: 'Modèle',
            placeholder: 'Clio, Corolla, etc.',
            required: true,
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
            required: true,
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
  ];

  pack: FormlyFieldConfig[] = [
    {
      key: 'name',
      type: 'radio',
      className: 'container', // Bootstrap container
      templateOptions: {
        label: 'Choisissez un Pack ',
        required: true,
        options: Object.keys(InsurancePackName).map(key => ({
          value: key,
          label: InsurancePackName[key as keyof typeof InsurancePackName],
        })),
      },
    },
  ];
}
