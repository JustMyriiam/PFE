import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IWarranty } from 'app/entities/insurance/warranty/warranty.model';
import { WarrantyService } from 'app/entities/insurance/warranty/service/warranty.service';
import { IVehicle } from 'app/entities/insurance/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/insurance/vehicle/service/vehicle.service';
import { IInsurancePack } from '../insurance-pack.model';
import { InsurancePackService } from '../service/insurance-pack.service';
import { InsurancePackFormService } from './insurance-pack-form.service';

import { InsurancePackUpdateComponent } from './insurance-pack-update.component';

describe('InsurancePack Management Update Component', () => {
  let comp: InsurancePackUpdateComponent;
  let fixture: ComponentFixture<InsurancePackUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let insurancePackFormService: InsurancePackFormService;
  let insurancePackService: InsurancePackService;
  let warrantyService: WarrantyService;
  let vehicleService: VehicleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [InsurancePackUpdateComponent],
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
      .overrideTemplate(InsurancePackUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InsurancePackUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    insurancePackFormService = TestBed.inject(InsurancePackFormService);
    insurancePackService = TestBed.inject(InsurancePackService);
    warrantyService = TestBed.inject(WarrantyService);
    vehicleService = TestBed.inject(VehicleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Warranty query and add missing value', () => {
      const insurancePack: IInsurancePack = { id: '943cf0ed-77cb-4f7b-9b93-9996ea6ef046' };
      const warranties: IWarranty[] = [{ id: '02209a44-8170-48fc-bfe9-76651f25b708' }];
      insurancePack.warranties = warranties;

      const warrantyCollection: IWarranty[] = [{ id: '02209a44-8170-48fc-bfe9-76651f25b708' }];
      jest.spyOn(warrantyService, 'query').mockReturnValue(of(new HttpResponse({ body: warrantyCollection })));
      const additionalWarranties = [...warranties];
      const expectedCollection: IWarranty[] = [...additionalWarranties, ...warrantyCollection];
      jest.spyOn(warrantyService, 'addWarrantyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ insurancePack });
      comp.ngOnInit();

      expect(warrantyService.query).toHaveBeenCalled();
      expect(warrantyService.addWarrantyToCollectionIfMissing).toHaveBeenCalledWith(
        warrantyCollection,
        ...additionalWarranties.map(expect.objectContaining),
      );
      expect(comp.warrantiesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Vehicle query and add missing value', () => {
      const insurancePack: IInsurancePack = { id: '943cf0ed-77cb-4f7b-9b93-9996ea6ef046' };
      const vehicle: IVehicle = { id: '9bb49fc1-a9df-4adb-bc15-4a7db764498f' };
      insurancePack.vehicle = vehicle;

      const vehicleCollection: IVehicle[] = [{ id: '9bb49fc1-a9df-4adb-bc15-4a7db764498f' }];
      jest.spyOn(vehicleService, 'query').mockReturnValue(of(new HttpResponse({ body: vehicleCollection })));
      const additionalVehicles = [vehicle];
      const expectedCollection: IVehicle[] = [...additionalVehicles, ...vehicleCollection];
      jest.spyOn(vehicleService, 'addVehicleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ insurancePack });
      comp.ngOnInit();

      expect(vehicleService.query).toHaveBeenCalled();
      expect(vehicleService.addVehicleToCollectionIfMissing).toHaveBeenCalledWith(
        vehicleCollection,
        ...additionalVehicles.map(expect.objectContaining),
      );
      expect(comp.vehiclesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const insurancePack: IInsurancePack = { id: '943cf0ed-77cb-4f7b-9b93-9996ea6ef046' };
      const warranties: IWarranty = { id: '02209a44-8170-48fc-bfe9-76651f25b708' };
      insurancePack.warranties = [warranties];
      const vehicle: IVehicle = { id: '9bb49fc1-a9df-4adb-bc15-4a7db764498f' };
      insurancePack.vehicle = vehicle;

      activatedRoute.data = of({ insurancePack });
      comp.ngOnInit();

      expect(comp.warrantiesSharedCollection).toContainEqual(warranties);
      expect(comp.vehiclesSharedCollection).toContainEqual(vehicle);
      expect(comp.insurancePack).toEqual(insurancePack);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsurancePack>>();
      const insurancePack = { id: '8d9bc0f2-5eaa-49fe-9c77-4ff4dd0af561' };
      jest.spyOn(insurancePackFormService, 'getInsurancePack').mockReturnValue(insurancePack);
      jest.spyOn(insurancePackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insurancePack });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: insurancePack }));
      saveSubject.complete();

      // THEN
      expect(insurancePackFormService.getInsurancePack).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(insurancePackService.update).toHaveBeenCalledWith(expect.objectContaining(insurancePack));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsurancePack>>();
      const insurancePack = { id: '8d9bc0f2-5eaa-49fe-9c77-4ff4dd0af561' };
      jest.spyOn(insurancePackFormService, 'getInsurancePack').mockReturnValue({ id: null });
      jest.spyOn(insurancePackService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insurancePack: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: insurancePack }));
      saveSubject.complete();

      // THEN
      expect(insurancePackFormService.getInsurancePack).toHaveBeenCalled();
      expect(insurancePackService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsurancePack>>();
      const insurancePack = { id: '8d9bc0f2-5eaa-49fe-9c77-4ff4dd0af561' };
      jest.spyOn(insurancePackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insurancePack });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(insurancePackService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareWarranty', () => {
      it('should forward to warrantyService', () => {
        const entity = { id: '02209a44-8170-48fc-bfe9-76651f25b708' };
        const entity2 = { id: '0e791b1b-70a5-4e8a-8534-6a5596acad14' };
        jest.spyOn(warrantyService, 'compareWarranty');
        comp.compareWarranty(entity, entity2);
        expect(warrantyService.compareWarranty).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVehicle', () => {
      it('should forward to vehicleService', () => {
        const entity = { id: '9bb49fc1-a9df-4adb-bc15-4a7db764498f' };
        const entity2 = { id: '941cfd1a-85dd-4b32-ba45-08e99fd1406f' };
        jest.spyOn(vehicleService, 'compareVehicle');
        comp.compareVehicle(entity, entity2);
        expect(vehicleService.compareVehicle).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
