import { Component, model } from '@angular/core';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { FormlyModule } from '@ngx-formly/core';
import { FormlyBootstrapModule } from '@ngx-formly/bootstrap';
import { JsonPipe } from '@angular/common';

@Component({
  selector: 'jhi-stepper-1',
  imports: [FormlyModule, FormlyBootstrapModule, ReactiveFormsModule, JsonPipe],
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
      validation: {
        messages: {
          required: 'Veuillez spécifier le ticket ConnectWise auquel ce ticket utilisateur est lié.',
        },
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
  protected readonly model = model;
  novalidate: any;
}
