import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IVehicle } from 'app/entities/insurance/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/insurance/vehicle/service/vehicle.service';
import { IClient } from 'app/entities/insurance/client/client.model';
import { ClientService } from 'app/entities/insurance/client/service/client.service';
import { IAgency } from 'app/entities/insurance/agency/agency.model';
import { AgencyService } from 'app/entities/insurance/agency/service/agency.service';
import { IContract } from '../contract.model';
import { ContractService } from '../service/contract.service';
import { ContractFormService } from './contract-form.service';

import { ContractUpdateComponent } from './contract-update.component';

describe('Contract Management Update Component', () => {
  let comp: ContractUpdateComponent;
  let fixture: ComponentFixture<ContractUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contractFormService: ContractFormService;
  let contractService: ContractService;
  let vehicleService: VehicleService;
  let clientService: ClientService;
  let agencyService: AgencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ContractUpdateComponent],
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
      .overrideTemplate(ContractUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContractUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contractFormService = TestBed.inject(ContractFormService);
    contractService = TestBed.inject(ContractService);
    vehicleService = TestBed.inject(VehicleService);
    clientService = TestBed.inject(ClientService);
    agencyService = TestBed.inject(AgencyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call vehicle query and add missing value', () => {
      const contract: IContract = { id: '1c1580c0-eda0-4eb0-b560-f2603bd5cf93' };
      const vehicle: IVehicle = { id: '9bb49fc1-a9df-4adb-bc15-4a7db764498f' };
      contract.vehicle = vehicle;

      const vehicleCollection: IVehicle[] = [{ id: '9bb49fc1-a9df-4adb-bc15-4a7db764498f' }];
      jest.spyOn(vehicleService, 'query').mockReturnValue(of(new HttpResponse({ body: vehicleCollection })));
      const expectedCollection: IVehicle[] = [vehicle, ...vehicleCollection];
      jest.spyOn(vehicleService, 'addVehicleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(vehicleService.query).toHaveBeenCalled();
      expect(vehicleService.addVehicleToCollectionIfMissing).toHaveBeenCalledWith(vehicleCollection, vehicle);
      expect(comp.vehiclesCollection).toEqual(expectedCollection);
    });

    it('should call Client query and add missing value', () => {
      const contract: IContract = { id: '1c1580c0-eda0-4eb0-b560-f2603bd5cf93' };
      const client: IClient = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      contract.client = client;

      const clientCollection: IClient[] = [{ id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(
        clientCollection,
        ...additionalClients.map(expect.objectContaining),
      );
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Agency query and add missing value', () => {
      const contract: IContract = { id: '1c1580c0-eda0-4eb0-b560-f2603bd5cf93' };
      const agency: IAgency = { id: '4b7ed6c2-7221-414a-93bf-e1ec02bf3053' };
      contract.agency = agency;

      const agencyCollection: IAgency[] = [{ id: '4b7ed6c2-7221-414a-93bf-e1ec02bf3053' }];
      jest.spyOn(agencyService, 'query').mockReturnValue(of(new HttpResponse({ body: agencyCollection })));
      const additionalAgencies = [agency];
      const expectedCollection: IAgency[] = [...additionalAgencies, ...agencyCollection];
      jest.spyOn(agencyService, 'addAgencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(agencyService.query).toHaveBeenCalled();
      expect(agencyService.addAgencyToCollectionIfMissing).toHaveBeenCalledWith(
        agencyCollection,
        ...additionalAgencies.map(expect.objectContaining),
      );
      expect(comp.agenciesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const contract: IContract = { id: '1c1580c0-eda0-4eb0-b560-f2603bd5cf93' };
      const vehicle: IVehicle = { id: '9bb49fc1-a9df-4adb-bc15-4a7db764498f' };
      contract.vehicle = vehicle;
      const client: IClient = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      contract.client = client;
      const agency: IAgency = { id: '4b7ed6c2-7221-414a-93bf-e1ec02bf3053' };
      contract.agency = agency;

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(comp.vehiclesCollection).toContainEqual(vehicle);
      expect(comp.clientsSharedCollection).toContainEqual(client);
      expect(comp.agenciesSharedCollection).toContainEqual(agency);
      expect(comp.contract).toEqual(contract);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' };
      jest.spyOn(contractFormService, 'getContract').mockReturnValue(contract);
      jest.spyOn(contractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contract }));
      saveSubject.complete();

      // THEN
      expect(contractFormService.getContract).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contractService.update).toHaveBeenCalledWith(expect.objectContaining(contract));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' };
      jest.spyOn(contractFormService, 'getContract').mockReturnValue({ id: null });
      jest.spyOn(contractService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contract }));
      saveSubject.complete();

      // THEN
      expect(contractFormService.getContract).toHaveBeenCalled();
      expect(contractService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' };
      jest.spyOn(contractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contractService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVehicle', () => {
      it('should forward to vehicleService', () => {
        const entity = { id: '9bb49fc1-a9df-4adb-bc15-4a7db764498f' };
        const entity2 = { id: '941cfd1a-85dd-4b32-ba45-08e99fd1406f' };
        jest.spyOn(vehicleService, 'compareVehicle');
        comp.compareVehicle(entity, entity2);
        expect(vehicleService.compareVehicle).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareClient', () => {
      it('should forward to clientService', () => {
        const entity = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
        const entity2 = { id: '687bb8c0-68bf-47c8-b217-55897bbb16b1' };
        jest.spyOn(clientService, 'compareClient');
        comp.compareClient(entity, entity2);
        expect(clientService.compareClient).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAgency', () => {
      it('should forward to agencyService', () => {
        const entity = { id: '4b7ed6c2-7221-414a-93bf-e1ec02bf3053' };
        const entity2 = { id: 'c64bfa10-4e04-4bbe-afe6-7ad1cca1790d' };
        jest.spyOn(agencyService, 'compareAgency');
        comp.compareAgency(entity, entity2);
        expect(agencyService.compareAgency).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
