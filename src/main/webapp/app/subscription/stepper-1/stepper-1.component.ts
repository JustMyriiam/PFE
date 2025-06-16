import { Component, model } from '@angular/core';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormlyModule } from '@ngx-formly/core';
import { FormlyBootstrapModule } from '@ngx-formly/bootstrap';
import { NgClass } from '@angular/common';
import { Step, StepList, StepPanel, StepPanels, Stepper } from 'primeng/stepper';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

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
  form = new FormGroup({});
  fields: FormlyFieldConfig[] = [
    {
      key: 'prenom',
      type: 'input',
      props: {
        label: 'Prénom',
        placeholder: 'Foulen',
        required: true,
      },
    },
    {
      key: 'nom',
      type: 'input',
      props: {
        label: 'Nom',
        placeholder: 'Ben Foulen',
        required: true,
      },
      validation: {
        messages: {
          required: 'Veuillez renseigner le nom.',
        },
      },
    },
    {
      key: 'mobile',
      type: 'input',
      props: {
        label: 'Mobile',
        placeholder: '01234567',
        required: true,
      },
      validation: {
        messages: {
          required: 'Veuillez entrer un numéro de mobile.',
          pattern: 'Le numéro de téléphone tunisien doit comporter 8 chiffres.',
        },
      },
    },
    {
      key: 'agence',
      type: 'input',
      props: {
        label: 'Agence: Gouvernorat, Agence',
        placeholder: 'Tunis, Agence XYZ',
        required: true,
      },
      validation: {
        messages: {
          required: "Veuillez spécifier l'agence et la localisation.",
        },
      },
    },
    {
      key: 'activite',
      type: 'input',
      props: {
        label: 'Activité',
        placeholder: 'Développement Logiciel',
        required: true,
      },
      validation: {
        messages: {
          required: 'Veuillez entrer votre activité.',
        },
      },
    },
    {
      key: 'situation_familiale',
      type: 'input',
      props: {
        label: 'Situation Familiale',
        placeholder: 'Marié / Célibataire',
        required: true,
      },
      validation: {
        messages: {
          required: 'Veuillez spécifier votre situation familiale.',
        },
      },
    },
    {
      key: 'adresse',
      type: 'input',
      props: {
        label: 'Adresse: Gouvernorat, Ville, Code Postal, Adresse',
        placeholder: 'Tunis, Tunis, 1000, Rue XYZ',
        required: true,
      },
      validation: {
        messages: {
          required: 'Veuillez fournir votre adresse complète.',
        },
      },
    },
  ];

  fieldsStep2: FormlyFieldConfig[] = [
    {
      key: 'registrationNumber',
      type: 'input',
      props: {
        label: 'Matricule',
        placeholder: '123 TU 456',
        required: true,
      },
    },
    {
      key: 'brand',
      type: 'input',
      props: {
        label: 'Marque',
        placeholder: 'Toyota, Renault, etc.',
        required: true,
      },
    },
    {
      key: 'model',
      type: 'input',
      props: {
        label: 'Modèle',
        placeholder: 'Clio, Corolla, etc.',
        required: true,
      },
    },
    {
      key: 'firstRegistrationDate',
      type: 'datepicker',
      props: {
        label: 'Date de première mise en circulation',
        required: true,
      },
    },
    {
      key: 'fiscalPower',
      type: 'input',
      props: {
        label: 'Puissance fiscale',
        placeholder: '6',
        type: 'number',
      },
    },
    {
      key: 'mileage',
      type: 'input',
      props: {
        label: 'Kilométrage',
        placeholder: 'ex: 120000',
        type: 'number',
      },
    },
    {
      key: 'color',
      type: 'input',
      props: {
        label: 'Couleur',
        placeholder: 'ex: Noir, Blanc',
      },
    },
    {
      key: 'gearbox',
      type: 'select',
      props: {
        label: 'Boîte de vitesse',
        options: [
          { label: 'Manuelle', value: 'MANUAL' },
          { label: 'Automatique', value: 'AUTOMATIC' },
        ],
      },
    },
    {
      key: 'energy',
      type: 'select',
      props: {
        label: 'Énergie',
        options: [
          { label: 'Essence', value: 'ESSENCE' },
          { label: 'Diesel', value: 'DIESEL' },
          { label: 'Électrique', value: 'ELECTRIC' },
          { label: 'Hybride', value: 'HYBRID' },
        ],
      },
    },
    {
      key: 'isNew',
      type: 'checkbox',
      props: {
        label: 'Véhicule Neuf ?',
      },
    },
  ];

  protected readonly model = model;
  novalidate: any;
  activeStep: number = 0;
}
