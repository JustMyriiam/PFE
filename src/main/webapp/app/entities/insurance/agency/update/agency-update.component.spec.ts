import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICity } from 'app/entities/insurance/city/city.model';
import { CityService } from 'app/entities/insurance/city/service/city.service';
import { AgencyService } from '../service/agency.service';
import { IAgency } from '../agency.model';
import { AgencyFormService } from './agency-form.service';

import { AgencyUpdateComponent } from './agency-update.component';

describe('Agency Management Update Component', () => {
  let comp: AgencyUpdateComponent;
  let fixture: ComponentFixture<AgencyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let agencyFormService: AgencyFormService;
  let agencyService: AgencyService;
  let cityService: CityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AgencyUpdateComponent],
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
      .overrideTemplate(AgencyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgencyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    agencyFormService = TestBed.inject(AgencyFormService);
    agencyService = TestBed.inject(AgencyService);
    cityService = TestBed.inject(CityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call City query and add missing value', () => {
      const agency: IAgency = { id: 'c64bfa10-4e04-4bbe-afe6-7ad1cca1790d' };
      const city: ICity = { id: '23758c6a-e922-4595-b3e2-be143d026188' };
      agency.city = city;

      const cityCollection: ICity[] = [{ id: '23758c6a-e922-4595-b3e2-be143d026188' }];
      jest.spyOn(cityService, 'query').mockReturnValue(of(new HttpResponse({ body: cityCollection })));
      const additionalCities = [city];
      const expectedCollection: ICity[] = [...additionalCities, ...cityCollection];
      jest.spyOn(cityService, 'addCityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agency });
      comp.ngOnInit();

      expect(cityService.query).toHaveBeenCalled();
      expect(cityService.addCityToCollectionIfMissing).toHaveBeenCalledWith(
        cityCollection,
        ...additionalCities.map(expect.objectContaining),
      );
      expect(comp.citiesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const agency: IAgency = { id: 'c64bfa10-4e04-4bbe-afe6-7ad1cca1790d' };
      const city: ICity = { id: '23758c6a-e922-4595-b3e2-be143d026188' };
      agency.city = city;

      activatedRoute.data = of({ agency });
      comp.ngOnInit();

      expect(comp.citiesSharedCollection).toContainEqual(city);
      expect(comp.agency).toEqual(agency);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgency>>();
      const agency = { id: '4b7ed6c2-7221-414a-93bf-e1ec02bf3053' };
      jest.spyOn(agencyFormService, 'getAgency').mockReturnValue(agency);
      jest.spyOn(agencyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agency });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agency }));
      saveSubject.complete();

      // THEN
      expect(agencyFormService.getAgency).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(agencyService.update).toHaveBeenCalledWith(expect.objectContaining(agency));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgency>>();
      const agency = { id: '4b7ed6c2-7221-414a-93bf-e1ec02bf3053' };
      jest.spyOn(agencyFormService, 'getAgency').mockReturnValue({ id: null });
      jest.spyOn(agencyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agency: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agency }));
      saveSubject.complete();

      // THEN
      expect(agencyFormService.getAgency).toHaveBeenCalled();
      expect(agencyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgency>>();
      const agency = { id: '4b7ed6c2-7221-414a-93bf-e1ec02bf3053' };
      jest.spyOn(agencyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agency });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(agencyService.update).toHaveBeenCalled();
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
