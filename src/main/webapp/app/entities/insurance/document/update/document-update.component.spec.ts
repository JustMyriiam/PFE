import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IContract } from 'app/entities/insurance/contract/contract.model';
import { ContractService } from 'app/entities/insurance/contract/service/contract.service';
import { DocumentService } from '../service/document.service';
import { IDocument } from '../document.model';
import { DocumentFormService } from './document-form.service';

import { DocumentUpdateComponent } from './document-update.component';

describe('Document Management Update Component', () => {
  let comp: DocumentUpdateComponent;
  let fixture: ComponentFixture<DocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentFormService: DocumentFormService;
  let documentService: DocumentService;
  let contractService: ContractService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentUpdateComponent],
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
      .overrideTemplate(DocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentFormService = TestBed.inject(DocumentFormService);
    documentService = TestBed.inject(DocumentService);
    contractService = TestBed.inject(ContractService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Contract query and add missing value', () => {
      const document: IDocument = { id: 'd72ebdf6-81bc-4a5d-8e29-45483c0d98b2' };
      const contract: IContract = { id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' };
      document.contract = contract;

      const contractCollection: IContract[] = [{ id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const additionalContracts = [contract];
      const expectedCollection: IContract[] = [...additionalContracts, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(
        contractCollection,
        ...additionalContracts.map(expect.objectContaining),
      );
      expect(comp.contractsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const document: IDocument = { id: 'd72ebdf6-81bc-4a5d-8e29-45483c0d98b2' };
      const contract: IContract = { id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' };
      document.contract = contract;

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(comp.contractsSharedCollection).toContainEqual(contract);
      expect(comp.document).toEqual(document);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocument>>();
      const document = { id: 'c1d4f1eb-eff0-4815-be04-c0d821e59542' };
      jest.spyOn(documentFormService, 'getDocument').mockReturnValue(document);
      jest.spyOn(documentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: document }));
      saveSubject.complete();

      // THEN
      expect(documentFormService.getDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentService.update).toHaveBeenCalledWith(expect.objectContaining(document));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocument>>();
      const document = { id: 'c1d4f1eb-eff0-4815-be04-c0d821e59542' };
      jest.spyOn(documentFormService, 'getDocument').mockReturnValue({ id: null });
      jest.spyOn(documentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: document }));
      saveSubject.complete();

      // THEN
      expect(documentFormService.getDocument).toHaveBeenCalled();
      expect(documentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocument>>();
      const document = { id: 'c1d4f1eb-eff0-4815-be04-c0d821e59542' };
      jest.spyOn(documentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContract', () => {
      it('should forward to contractService', () => {
        const entity = { id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' };
        const entity2 = { id: '1c1580c0-eda0-4eb0-b560-f2603bd5cf93' };
        jest.spyOn(contractService, 'compareContract');
        comp.compareContract(entity, entity2);
        expect(contractService.compareContract).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
