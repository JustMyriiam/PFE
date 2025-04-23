import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IContract } from '../contract.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../contract.test-samples';

import { ContractService, RestContract } from './contract.service';

const requireRestSample: RestContract = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('Contract Service', () => {
  let service: ContractService;
  let httpMock: HttpTestingController;
  let expectedResult: IContract | IContract[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ContractService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Contract', () => {
      const contract = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contract).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Contract', () => {
      const contract = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contract).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Contract', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Contract', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Contract', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Contract', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addContractToCollectionIfMissing', () => {
      it('should add a Contract to an empty array', () => {
        const contract: IContract = sampleWithRequiredData;
        expectedResult = service.addContractToCollectionIfMissing([], contract);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contract);
      });

      it('should not add a Contract to an array that contains it', () => {
        const contract: IContract = sampleWithRequiredData;
        const contractCollection: IContract[] = [
          {
            ...contract,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContractToCollectionIfMissing(contractCollection, contract);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Contract to an array that doesn't contain it", () => {
        const contract: IContract = sampleWithRequiredData;
        const contractCollection: IContract[] = [sampleWithPartialData];
        expectedResult = service.addContractToCollectionIfMissing(contractCollection, contract);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contract);
      });

      it('should add only unique Contract to an array', () => {
        const contractArray: IContract[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contractCollection: IContract[] = [sampleWithRequiredData];
        expectedResult = service.addContractToCollectionIfMissing(contractCollection, ...contractArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contract: IContract = sampleWithRequiredData;
        const contract2: IContract = sampleWithPartialData;
        expectedResult = service.addContractToCollectionIfMissing([], contract, contract2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contract);
        expect(expectedResult).toContain(contract2);
      });

      it('should accept null and undefined values', () => {
        const contract: IContract = sampleWithRequiredData;
        expectedResult = service.addContractToCollectionIfMissing([], null, contract, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contract);
      });

      it('should return initial array if no Contract is added', () => {
        const contractCollection: IContract[] = [sampleWithRequiredData];
        expectedResult = service.addContractToCollectionIfMissing(contractCollection, undefined, null);
        expect(expectedResult).toEqual(contractCollection);
      });
    });

    describe('compareContract', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContract(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' };
        const entity2 = null;

        const compareResult1 = service.compareContract(entity1, entity2);
        const compareResult2 = service.compareContract(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' };
        const entity2 = { id: '1c1580c0-eda0-4eb0-b560-f2603bd5cf93' };

        const compareResult1 = service.compareContract(entity1, entity2);
        const compareResult2 = service.compareContract(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' };
        const entity2 = { id: 'c75fa77d-c676-4967-8932-ebc2af9e9bf2' };

        const compareResult1 = service.compareContract(entity1, entity2);
        const compareResult2 = service.compareContract(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
