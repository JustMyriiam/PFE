import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IVehicle } from 'app/entities/insurance/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/insurance/vehicle/service/vehicle.service';
import { IClient } from 'app/entities/insurance/client/client.model';
import { ClientService } from 'app/entities/insurance/client/service/client.service';
import { IQuote } from '../quote.model';
import { QuoteService } from '../service/quote.service';
import { QuoteFormService } from './quote-form.service';

import { QuoteUpdateComponent } from './quote-update.component';

describe('Quote Management Update Component', () => {
  let comp: QuoteUpdateComponent;
  let fixture: ComponentFixture<QuoteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quoteFormService: QuoteFormService;
  let quoteService: QuoteService;
  let vehicleService: VehicleService;
  let clientService: ClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [QuoteUpdateComponent],
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
      .overrideTemplate(QuoteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuoteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quoteFormService = TestBed.inject(QuoteFormService);
    quoteService = TestBed.inject(QuoteService);
    vehicleService = TestBed.inject(VehicleService);
    clientService = TestBed.inject(ClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call vehicle query and add missing value', () => {
      const quote: IQuote = { id: '7e96257b-a9d4-4759-88b1-865c11475b23' };
      const vehicle: IVehicle = { id: '9bb49fc1-a9df-4adb-bc15-4a7db764498f' };
      quote.vehicle = vehicle;

      const vehicleCollection: IVehicle[] = [{ id: '9bb49fc1-a9df-4adb-bc15-4a7db764498f' }];
      jest.spyOn(vehicleService, 'query').mockReturnValue(of(new HttpResponse({ body: vehicleCollection })));
      const expectedCollection: IVehicle[] = [vehicle, ...vehicleCollection];
      jest.spyOn(vehicleService, 'addVehicleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      expect(vehicleService.query).toHaveBeenCalled();
      expect(vehicleService.addVehicleToCollectionIfMissing).toHaveBeenCalledWith(vehicleCollection, vehicle);
      expect(comp.vehiclesCollection).toEqual(expectedCollection);
    });

    it('should call Client query and add missing value', () => {
      const quote: IQuote = { id: '7e96257b-a9d4-4759-88b1-865c11475b23' };
      const client: IClient = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      quote.client = client;

      const clientCollection: IClient[] = [{ id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(
        clientCollection,
        ...additionalClients.map(expect.objectContaining),
      );
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const quote: IQuote = { id: '7e96257b-a9d4-4759-88b1-865c11475b23' };
      const vehicle: IVehicle = { id: '9bb49fc1-a9df-4adb-bc15-4a7db764498f' };
      quote.vehicle = vehicle;
      const client: IClient = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      quote.client = client;

      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      expect(comp.vehiclesCollection).toContainEqual(vehicle);
      expect(comp.clientsSharedCollection).toContainEqual(client);
      expect(comp.quote).toEqual(quote);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuote>>();
      const quote = { id: '4bc6776a-0dac-4853-b88a-48033f316c97' };
      jest.spyOn(quoteFormService, 'getQuote').mockReturnValue(quote);
      jest.spyOn(quoteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quote }));
      saveSubject.complete();

      // THEN
      expect(quoteFormService.getQuote).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quoteService.update).toHaveBeenCalledWith(expect.objectContaining(quote));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuote>>();
      const quote = { id: '4bc6776a-0dac-4853-b88a-48033f316c97' };
      jest.spyOn(quoteFormService, 'getQuote').mockReturnValue({ id: null });
      jest.spyOn(quoteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quote: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quote }));
      saveSubject.complete();

      // THEN
      expect(quoteFormService.getQuote).toHaveBeenCalled();
      expect(quoteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuote>>();
      const quote = { id: '4bc6776a-0dac-4853-b88a-48033f316c97' };
      jest.spyOn(quoteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quoteService.update).toHaveBeenCalled();
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
  });
});
