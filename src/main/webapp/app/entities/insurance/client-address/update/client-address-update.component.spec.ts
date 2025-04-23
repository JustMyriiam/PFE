import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICity } from 'app/entities/insurance/city/city.model';
import { CityService } from 'app/entities/insurance/city/service/city.service';
import { ClientAddressService } from '../service/client-address.service';
import { IClientAddress } from '../client-address.model';
import { ClientAddressFormService } from './client-address-form.service';

import { ClientAddressUpdateComponent } from './client-address-update.component';

describe('ClientAddress Management Update Component', () => {
  let comp: ClientAddressUpdateComponent;
  let fixture: ComponentFixture<ClientAddressUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clientAddressFormService: ClientAddressFormService;
  let clientAddressService: ClientAddressService;
  let cityService: CityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ClientAddressUpdateComponent],
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
      .overrideTemplate(ClientAddressUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClientAddressUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clientAddressFormService = TestBed.inject(ClientAddressFormService);
    clientAddressService = TestBed.inject(ClientAddressService);
    cityService = TestBed.inject(CityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call City query and add missing value', () => {
      const clientAddress: IClientAddress = { id: '0a995b15-88f9-49d9-a2a6-95d4ec3e5923' };
      const city: ICity = { id: '23758c6a-e922-4595-b3e2-be143d026188' };
      clientAddress.city = city;

      const cityCollection: ICity[] = [{ id: '23758c6a-e922-4595-b3e2-be143d026188' }];
      jest.spyOn(cityService, 'query').mockReturnValue(of(new HttpResponse({ body: cityCollection })));
      const additionalCities = [city];
      const expectedCollection: ICity[] = [...additionalCities, ...cityCollection];
      jest.spyOn(cityService, 'addCityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ clientAddress });
      comp.ngOnInit();

      expect(cityService.query).toHaveBeenCalled();
      expect(cityService.addCityToCollectionIfMissing).toHaveBeenCalledWith(
        cityCollection,
        ...additionalCities.map(expect.objectContaining),
      );
      expect(comp.citiesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const clientAddress: IClientAddress = { id: '0a995b15-88f9-49d9-a2a6-95d4ec3e5923' };
      const city: ICity = { id: '23758c6a-e922-4595-b3e2-be143d026188' };
      clientAddress.city = city;

      activatedRoute.data = of({ clientAddress });
      comp.ngOnInit();

      expect(comp.citiesSharedCollection).toContainEqual(city);
      expect(comp.clientAddress).toEqual(clientAddress);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClientAddress>>();
      const clientAddress = { id: 'ba359270-b697-420f-9c86-ece18688b5ef' };
      jest.spyOn(clientAddressFormService, 'getClientAddress').mockReturnValue(clientAddress);
      jest.spyOn(clientAddressService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clientAddress });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clientAddress }));
      saveSubject.complete();

      // THEN
      expect(clientAddressFormService.getClientAddress).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(clientAddressService.update).toHaveBeenCalledWith(expect.objectContaining(clientAddress));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClientAddress>>();
      const clientAddress = { id: 'ba359270-b697-420f-9c86-ece18688b5ef' };
      jest.spyOn(clientAddressFormService, 'getClientAddress').mockReturnValue({ id: null });
      jest.spyOn(clientAddressService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clientAddress: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clientAddress }));
      saveSubject.complete();

      // THEN
      expect(clientAddressFormService.getClientAddress).toHaveBeenCalled();
      expect(clientAddressService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClientAddress>>();
      const clientAddress = { id: 'ba359270-b697-420f-9c86-ece18688b5ef' };
      jest.spyOn(clientAddressService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clientAddress });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clientAddressService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCity', () => {
      it('should forward to cityService', () => {
        const entity = { id: '23758c6a-e922-4595-b3e2-be143d026188' };
        const entity2 = { id: 'caae2f98-09cb-467a-ae94-505a232ee840' };
        jest.spyOn(cityService, 'compareCity');
        comp.compareCity(entity, entity2);
        expect(cityService.compareCity).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
