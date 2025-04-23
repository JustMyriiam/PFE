import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IGovernorate } from 'app/entities/insurance/governorate/governorate.model';
import { GovernorateService } from 'app/entities/insurance/governorate/service/governorate.service';
import { CityService } from '../service/city.service';
import { ICity } from '../city.model';
import { CityFormService } from './city-form.service';

import { CityUpdateComponent } from './city-update.component';

describe('City Management Update Component', () => {
  let comp: CityUpdateComponent;
  let fixture: ComponentFixture<CityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cityFormService: CityFormService;
  let cityService: CityService;
  let governorateService: GovernorateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CityUpdateComponent],
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
      .overrideTemplate(CityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cityFormService = TestBed.inject(CityFormService);
    cityService = TestBed.inject(CityService);
    governorateService = TestBed.inject(GovernorateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Governorate query and add missing value', () => {
      const city: ICity = { id: 'caae2f98-09cb-467a-ae94-505a232ee840' };
      const governorate: IGovernorate = { id: '06b5bd02-0f85-4d95-9111-578d64b53b5c' };
      city.governorate = governorate;

      const governorateCollection: IGovernorate[] = [{ id: '06b5bd02-0f85-4d95-9111-578d64b53b5c' }];
      jest.spyOn(governorateService, 'query').mockReturnValue(of(new HttpResponse({ body: governorateCollection })));
      const additionalGovernorates = [governorate];
      const expectedCollection: IGovernorate[] = [...additionalGovernorates, ...governorateCollection];
      jest.spyOn(governorateService, 'addGovernorateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ city });
      comp.ngOnInit();

      expect(governorateService.query).toHaveBeenCalled();
      expect(governorateService.addGovernorateToCollectionIfMissing).toHaveBeenCalledWith(
        governorateCollection,
        ...additionalGovernorates.map(expect.objectContaining),
      );
      expect(comp.governoratesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const city: ICity = { id: 'caae2f98-09cb-467a-ae94-505a232ee840' };
      const governorate: IGovernorate = { id: '06b5bd02-0f85-4d95-9111-578d64b53b5c' };
      city.governorate = governorate;

      activatedRoute.data = of({ city });
      comp.ngOnInit();

      expect(comp.governoratesSharedCollection).toContainEqual(governorate);
      expect(comp.city).toEqual(city);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICity>>();
      const city = { id: '23758c6a-e922-4595-b3e2-be143d026188' };
      jest.spyOn(cityFormService, 'getCity').mockReturnValue(city);
      jest.spyOn(cityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ city });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: city }));
      saveSubject.complete();

      // THEN
      expect(cityFormService.getCity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cityService.update).toHaveBeenCalledWith(expect.objectContaining(city));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICity>>();
      const city = { id: '23758c6a-e922-4595-b3e2-be143d026188' };
      jest.spyOn(cityFormService, 'getCity').mockReturnValue({ id: null });
      jest.spyOn(cityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ city: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: city }));
      saveSubject.complete();

      // THEN
      expect(cityFormService.getCity).toHaveBeenCalled();
      expect(cityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICity>>();
      const city = { id: '23758c6a-e922-4595-b3e2-be143d026188' };
      jest.spyOn(cityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ city });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGovernorate', () => {
      it('should forward to governorateService', () => {
        const entity = { id: '06b5bd02-0f85-4d95-9111-578d64b53b5c' };
        const entity2 = { id: '115a502d-2c89-49eb-b046-b41ce5c05608' };
        jest.spyOn(governorateService, 'compareGovernorate');
        comp.compareGovernorate(entity, entity2);
        expect(governorateService.compareGovernorate).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
