import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IClient } from 'app/entities/insurance/client/client.model';
import { ClientService } from 'app/entities/insurance/client/service/client.service';
import { IContract } from 'app/entities/insurance/contract/contract.model';
import { ContractService } from 'app/entities/insurance/contract/service/contract.service';
import { IClaim } from '../claim.model';
import { ClaimService } from '../service/claim.service';
import { ClaimFormService } from './claim-form.service';

import { ClaimUpdateComponent } from './claim-update.component';

describe('Claim Management Update Component', () => {
  let comp: ClaimUpdateComponent;
  let fixture: ComponentFixture<ClaimUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let claimFormService: ClaimFormService;
  let claimService: ClaimService;
  let clientService: ClientService;
  let contractService: ContractService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ClaimUpdateComponent],
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
      .overrideTemplate(ClaimUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClaimUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    claimFormService = TestBed.inject(ClaimFormService);
    claimService = TestBed.inject(ClaimService);
    clientService = TestBed.inject(ClientService);
    contractService = TestBed.inject(ContractService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Client query and add missing value', () => {
      const claim: IClaim = { id: '8bb3b970-e511-4c38-8959-e76645f63e0c' };
      const client: IClient = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      claim.client = client;

      const clientCollection: IClient[] = [{ id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ claim });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(
        clientCollection,
        ...additionalClients.map(expect.objectContaining),
      );
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Contract query and add missing value', () => {
      const claim: IClaim = { id: '8bb3b970-e511-4c38-8959-e76645f63e0c' };
      const contract: IContract = { id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' };
      claim.contract = contract;

      const contractCollection: IContract[] = [{ id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const additionalContracts = [contract];
      const expectedCollection: IContract[] = [...additionalContracts, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ claim });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(
        contractCollection,
        ...additionalContracts.map(expect.objectContaining),
      );
      expect(comp.contractsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const claim: IClaim = { id: '8bb3b970-e511-4c38-8959-e76645f63e0c' };
      const client: IClient = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      claim.client = client;
      const contract: IContract = { id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' };
      claim.contract = contract;

      activatedRoute.data = of({ claim });
      comp.ngOnInit();

      expect(comp.clientsSharedCollection).toContainEqual(client);
      expect(comp.contractsSharedCollection).toContainEqual(contract);
      expect(comp.claim).toEqual(claim);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClaim>>();
      const claim = { id: '94ec77c2-f5a1-41a3-b8f8-7bf9e231e536' };
      jest.spyOn(claimFormService, 'getClaim').mockReturnValue(claim);
      jest.spyOn(claimService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ claim });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: claim }));
      saveSubject.complete();

      // THEN
      expect(claimFormService.getClaim).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(claimService.update).toHaveBeenCalledWith(expect.objectContaining(claim));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClaim>>();
      const claim = { id: '94ec77c2-f5a1-41a3-b8f8-7bf9e231e536' };
      jest.spyOn(claimFormService, 'getClaim').mockReturnValue({ id: null });
      jest.spyOn(claimService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ claim: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: claim }));
      saveSubject.complete();

      // THEN
      expect(claimFormService.getClaim).toHaveBeenCalled();
      expect(claimService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClaim>>();
      const claim = { id: '94ec77c2-f5a1-41a3-b8f8-7bf9e231e536' };
      jest.spyOn(claimService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ claim });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(claimService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareClient', () => {
      it('should forward to clientService', () => {
        const entity = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
        const entity2 = { id: '687bb8c0-68bf-47c8-b217-55897bbb16b1' };
        jest.spyOn(clientService, 'compareClient');
        comp.compareClient(entity, entity2);
        expect(clientService.compareClient).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
