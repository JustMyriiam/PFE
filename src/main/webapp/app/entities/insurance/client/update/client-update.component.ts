import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IClientAddress } from 'app/entities/insurance/client-address/client-address.model';
import { ClientAddressService } from 'app/entities/insurance/client-address/service/client-address.service';
import { IdentityType } from 'app/entities/enumerations/identity-type.model';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { ProfessionalStatus } from 'app/entities/enumerations/professional-status.model';
import { ClientService } from '../service/client.service';
import { IClient } from '../client.model';
import { ClientFormGroup, ClientFormService } from './client-form.service';

@Component({
  selector: 'jhi-client-update',
  templateUrl: './client-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClientUpdateComponent implements OnInit {
  isSaving = false;
  client: IClient | null = null;
  identityTypeValues = Object.keys(IdentityType);
  maritalStatusValues = Object.keys(MaritalStatus);
  genderValues = Object.keys(Gender);
  professionalStatusValues = Object.keys(ProfessionalStatus);

  usersSharedCollection: IUser[] = [];
  clientAddressesSharedCollection: IClientAddress[] = [];

  protected clientService = inject(ClientService);
  protected clientFormService = inject(ClientFormService);
  protected userService = inject(UserService);
  protected clientAddressService = inject(ClientAddressService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ClientFormGroup = this.clientFormService.createClientFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareClientAddress = (o1: IClientAddress | null, o2: IClientAddress | null): boolean =>
    this.clientAddressService.compareClientAddress(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ client }) => {
      this.client = client;
      if (client) {
        this.updateForm(client);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const client = this.clientFormService.getClient(this.editForm);
    if (client.id !== null) {
      this.subscribeToSaveResponse(this.clientService.update(client));
    } else {
      this.subscribeToSaveResponse(this.clientService.create(client));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClient>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(client: IClient): void {
    this.client = client;
    this.clientFormService.resetForm(this.editForm, client);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, client.user);
    this.clientAddressesSharedCollection = this.clientAddressService.addClientAddressToCollectionIfMissing<IClientAddress>(
      this.clientAddressesSharedCollection,
      client.clientAddress,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.client?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.clientAddressService
      .query()
      .pipe(map((res: HttpResponse<IClientAddress[]>) => res.body ?? []))
      .pipe(
        map((clientAddresses: IClientAddress[]) =>
          this.clientAddressService.addClientAddressToCollectionIfMissing<IClientAddress>(clientAddresses, this.client?.clientAddress),
        ),
      )
      .subscribe((clientAddresses: IClientAddress[]) => (this.clientAddressesSharedCollection = clientAddresses));
  }
}
