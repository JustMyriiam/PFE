import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IInsurancePack } from '../insurance-pack.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../insurance-pack.test-samples';

import { InsurancePackService } from './insurance-pack.service';

const requireRestSample: IInsurancePack = {
  ...sampleWithRequiredData,
};

describe('InsurancePack Service', () => {
  let service: InsurancePackService;
  let httpMock: HttpTestingController;
  let expectedResult: IInsurancePack | IInsurancePack[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InsurancePackService);
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

    it('should create a InsurancePack', () => {
      const insurancePack = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(insurancePack).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InsurancePack', () => {
      const insurancePack = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(insurancePack).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InsurancePack', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InsurancePack', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InsurancePack', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a InsurancePack', () => {
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

    describe('addInsurancePackToCollectionIfMissing', () => {
      it('should add a InsurancePack to an empty array', () => {
        const insurancePack: IInsurancePack = sampleWithRequiredData;
        expectedResult = service.addInsurancePackToCollectionIfMissing([], insurancePack);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(insurancePack);
      });

      it('should not add a InsurancePack to an array that contains it', () => {
        const insurancePack: IInsurancePack = sampleWithRequiredData;
        const insurancePackCollection: IInsurancePack[] = [
          {
            ...insurancePack,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInsurancePackToCollectionIfMissing(insurancePackCollection, insurancePack);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InsurancePack to an array that doesn't contain it", () => {
        const insurancePack: IInsurancePack = sampleWithRequiredData;
        const insurancePackCollection: IInsurancePack[] = [sampleWithPartialData];
        expectedResult = service.addInsurancePackToCollectionIfMissing(insurancePackCollection, insurancePack);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(insurancePack);
      });

      it('should add only unique InsurancePack to an array', () => {
        const insurancePackArray: IInsurancePack[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const insurancePackCollection: IInsurancePack[] = [sampleWithRequiredData];
        expectedResult = service.addInsurancePackToCollectionIfMissing(insurancePackCollection, ...insurancePackArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const insurancePack: IInsurancePack = sampleWithRequiredData;
        const insurancePack2: IInsurancePack = sampleWithPartialData;
        expectedResult = service.addInsurancePackToCollectionIfMissing([], insurancePack, insurancePack2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(insurancePack);
        expect(expectedResult).toContain(insurancePack2);
      });

      it('should accept null and undefined values', () => {
        const insurancePack: IInsurancePack = sampleWithRequiredData;
        expectedResult = service.addInsurancePackToCollectionIfMissing([], null, insurancePack, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(insurancePack);
      });

      it('should return initial array if no InsurancePack is added', () => {
        const insurancePackCollection: IInsurancePack[] = [sampleWithRequiredData];
        expectedResult = service.addInsurancePackToCollectionIfMissing(insurancePackCollection, undefined, null);
        expect(expectedResult).toEqual(insurancePackCollection);
      });
    });

    describe('compareInsurancePack', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInsurancePack(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '8d9bc0f2-5eaa-49fe-9c77-4ff4dd0af561' };
        const entity2 = null;

        const compareResult1 = service.compareInsurancePack(entity1, entity2);
        const compareResult2 = service.compareInsurancePack(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '8d9bc0f2-5eaa-49fe-9c77-4ff4dd0af561' };
        const entity2 = { id: '943cf0ed-77cb-4f7b-9b93-9996ea6ef046' };

        const compareResult1 = service.compareInsurancePack(entity1, entity2);
        const compareResult2 = service.compareInsurancePack(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: '8d9bc0f2-5eaa-49fe-9c77-4ff4dd0af561' };
        const entity2 = { id: '8d9bc0f2-5eaa-49fe-9c77-4ff4dd0af561' };

        const compareResult1 = service.compareInsurancePack(entity1, entity2);
        const compareResult2 = service.compareInsurancePack(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
