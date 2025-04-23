import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IInsurancePack } from 'app/entities/insurance/insurance-pack/insurance-pack.model';
import { InsurancePackService } from 'app/entities/insurance/insurance-pack/service/insurance-pack.service';
import { WarrantyService } from '../service/warranty.service';
import { IWarranty } from '../warranty.model';
import { WarrantyFormService } from './warranty-form.service';

import { WarrantyUpdateComponent } from './warranty-update.component';

describe('Warranty Management Update Component', () => {
  let comp: WarrantyUpdateComponent;
  let fixture: ComponentFixture<WarrantyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let warrantyFormService: WarrantyFormService;
  let warrantyService: WarrantyService;
  let insurancePackService: InsurancePackService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WarrantyUpdateComponent],
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
      .overrideTemplate(WarrantyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WarrantyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    warrantyFormService = TestBed.inject(WarrantyFormService);
    warrantyService = TestBed.inject(WarrantyService);
    insurancePackService = TestBed.inject(InsurancePackService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call InsurancePack query and add missing value', () => {
      const warranty: IWarranty = { id: '0e791b1b-70a5-4e8a-8534-6a5596acad14' };
      const insurancePacks: IInsurancePack[] = [{ id: '8d9bc0f2-5eaa-49fe-9c77-4ff4dd0af561' }];
      warranty.insurancePacks = insurancePacks;

      const insurancePackCollection: IInsurancePack[] = [{ id: '8d9bc0f2-5eaa-49fe-9c77-4ff4dd0af561' }];
      jest.spyOn(insurancePackService, 'query').mockReturnValue(of(new HttpResponse({ body: insurancePackCollection })));
      const additionalInsurancePacks = [...insurancePacks];
      const expectedCollection: IInsurancePack[] = [...additionalInsurancePacks, ...insurancePackCollection];
      jest.spyOn(insurancePackService, 'addInsurancePackToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ warranty });
      comp.ngOnInit();

      expect(insurancePackService.query).toHaveBeenCalled();
      expect(insurancePackService.addInsurancePackToCollectionIfMissing).toHaveBeenCalledWith(
        insurancePackCollection,
        ...additionalInsurancePacks.map(expect.objectContaining),
      );
      expect(comp.insurancePacksSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const warranty: IWarranty = { id: '0e791b1b-70a5-4e8a-8534-6a5596acad14' };
      const insurancePacks: IInsurancePack = { id: '8d9bc0f2-5eaa-49fe-9c77-4ff4dd0af561' };
      warranty.insurancePacks = [insurancePacks];

      activatedRoute.data = of({ warranty });
      comp.ngOnInit();

      expect(comp.insurancePacksSharedCollection).toContainEqual(insurancePacks);
      expect(comp.warranty).toEqual(warranty);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWarranty>>();
      const warranty = { id: '02209a44-8170-48fc-bfe9-76651f25b708' };
      jest.spyOn(warrantyFormService, 'getWarranty').mockReturnValue(warranty);
      jest.spyOn(warrantyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warranty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: warranty }));
      saveSubject.complete();

      // THEN
      expect(warrantyFormService.getWarranty).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(warrantyService.update).toHaveBeenCalledWith(expect.objectContaining(warranty));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWarranty>>();
      const warranty = { id: '02209a44-8170-48fc-bfe9-76651f25b708' };
      jest.spyOn(warrantyFormService, 'getWarranty').mockReturnValue({ id: null });
      jest.spyOn(warrantyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warranty: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: warranty }));
      saveSubject.complete();

      // THEN
      expect(warrantyFormService.getWarranty).toHaveBeenCalled();
      expect(warrantyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWarranty>>();
      const warranty = { id: '02209a44-8170-48fc-bfe9-76651f25b708' };
      jest.spyOn(warrantyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warranty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(warrantyService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInsurancePack', () => {
      it('should forward to insurancePackService', () => {
        const entity = { id: '8d9bc0f2-5eaa-49fe-9c77-4ff4dd0af561' };
        const entity2 = { id: '943cf0ed-77cb-4f7b-9b93-9996ea6ef046' };
        jest.spyOn(insurancePackService, 'compareInsurancePack');
        comp.compareInsurancePack(entity, entity2);
        expect(insurancePackService.compareInsurancePack).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
