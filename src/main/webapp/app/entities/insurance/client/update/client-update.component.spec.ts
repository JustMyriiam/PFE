import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IClientAddress } from 'app/entities/insurance/client-address/client-address.model';
import { ClientAddressService } from 'app/entities/insurance/client-address/service/client-address.service';
import { IClient } from '../client.model';
import { ClientService } from '../service/client.service';
import { ClientFormService } from './client-form.service';

import { ClientUpdateComponent } from './client-update.component';

describe('Client Management Update Component', () => {
  let comp: ClientUpdateComponent;
  let fixture: ComponentFixture<ClientUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clientFormService: ClientFormService;
  let clientService: ClientService;
  let userService: UserService;
  let clientAddressService: ClientAddressService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ClientUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ClientUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClientUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clientFormService = TestBed.inject(ClientFormService);
    clientService = TestBed.inject(ClientService);
    userService = TestBed.inject(UserService);
    clientAddressService = TestBed.inject(ClientAddressService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const client: IClient = { id: '687bb8c0-68bf-47c8-b217-55897bbb16b1' };
      const user: IUser = { id: '1344246c-16a7-46d1-bb61-2043f965c8d5' };
      client.user = user;

      const userCollection: IUser[] = [{ id: '1344246c-16a7-46d1-bb61-2043f965c8d5' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ client });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('should call ClientAddress query and add missing value', () => {
      const client: IClient = { id: '687bb8c0-68bf-47c8-b217-55897bbb16b1' };
      const clientAddress: IClientAddress = { id: 'ba359270-b697-420f-9c86-ece18688b5ef' };
      client.clientAddress = clientAddress;

      const clientAddressCollection: IClientAddress[] = [{ id: 'ba359270-b697-420f-9c86-ece18688b5ef' }];
      jest.spyOn(clientAddressService, 'query').mockReturnValue(of(new HttpResponse({ body: clientAddressCollection })));
      const additionalClientAddresses = [clientAddress];
      const expectedCollection: IClientAddress[] = [...additionalClientAddresses, ...clientAddressCollection];
      jest.spyOn(clientAddressService, 'addClientAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ client });
      comp.ngOnInit();

      expect(clientAddressService.query).toHaveBeenCalled();
      expect(clientAddressService.addClientAddressToCollectionIfMissing).toHaveBeenCalledWith(
        clientAddressCollection,
        ...additionalClientAddresses.map(expect.objectContaining),
      );
      expect(comp.clientAddressesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const client: IClient = { id: '687bb8c0-68bf-47c8-b217-55897bbb16b1' };
      const user: IUser = { id: '1344246c-16a7-46d1-bb61-2043f965c8d5' };
      client.user = user;
      const clientAddress: IClientAddress = { id: 'ba359270-b697-420f-9c86-ece18688b5ef' };
      client.clientAddress = clientAddress;

      activatedRoute.data = of({ client });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContainEqual(user);
      expect(comp.clientAddressesSharedCollection).toContainEqual(clientAddress);
      expect(comp.client).toEqual(client);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClient>>();
      const client = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      jest.spyOn(clientFormService, 'getClient').mockReturnValue(client);
      jest.spyOn(clientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ client });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: client }));
      saveSubject.complete();

      // THEN
      expect(clientFormService.getClient).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(clientService.update).toHaveBeenCalledWith(expect.objectContaining(client));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClient>>();
      const client = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      jest.spyOn(clientFormService, 'getClient').mockReturnValue({ id: null });
      jest.spyOn(clientService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ client: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: client }));
      saveSubject.complete();

      // THEN
      expect(clientFormService.getClient).toHaveBeenCalled();
      expect(clientService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClient>>();
      const client = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      jest.spyOn(clientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ client });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clientService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: '1344246c-16a7-46d1-bb61-2043f965c8d5' };
        const entity2 = { id: '1e61df13-b2d3-459d-875e-5607a4ccdbdb' };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareClientAddress', () => {
      it('should forward to clientAddressService', () => {
        const entity = { id: 'ba359270-b697-420f-9c86-ece18688b5ef' };
        const entity2 = { id: '0a995b15-88f9-49d9-a2a6-95d4ec3e5923' };
        jest.spyOn(clientAddressService, 'compareClientAddress');
        comp.compareClientAddress(entity, entity2);
        expect(clientAddressService.compareClientAddress).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
