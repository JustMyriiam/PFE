import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IGovernorate } from '../governorate.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../governorate.test-samples';

import { GovernorateService } from './governorate.service';

const requireRestSample: IGovernorate = {
  ...sampleWithRequiredData,
};

describe('Governorate Service', () => {
  let service: GovernorateService;
  let httpMock: HttpTestingController;
  let expectedResult: IGovernorate | IGovernorate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(GovernorateService);
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

    it('should create a Governorate', () => {
      const governorate = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(governorate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Governorate', () => {
      const governorate = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(governorate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Governorate', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Governorate', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Governorate', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Governorate', () => {
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

    describe('addGovernorateToCollectionIfMissing', () => {
      it('should add a Governorate to an empty array', () => {
        const governorate: IGovernorate = sampleWithRequiredData;
        expectedResult = service.addGovernorateToCollectionIfMissing([], governorate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(governorate);
      });

      it('should not add a Governorate to an array that contains it', () => {
        const governorate: IGovernorate = sampleWithRequiredData;
        const governorateCollection: IGovernorate[] = [
          {
            ...governorate,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGovernorateToCollectionIfMissing(governorateCollection, governorate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Governorate to an array that doesn't contain it", () => {
        const governorate: IGovernorate = sampleWithRequiredData;
        const governorateCollection: IGovernorate[] = [sampleWithPartialData];
        expectedResult = service.addGovernorateToCollectionIfMissing(governorateCollection, governorate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(governorate);
      });

      it('should add only unique Governorate to an array', () => {
        const governorateArray: IGovernorate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const governorateCollection: IGovernorate[] = [sampleWithRequiredData];
        expectedResult = service.addGovernorateToCollectionIfMissing(governorateCollection, ...governorateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const governorate: IGovernorate = sampleWithRequiredData;
        const governorate2: IGovernorate = sampleWithPartialData;
        expectedResult = service.addGovernorateToCollectionIfMissing([], governorate, governorate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(governorate);
        expect(expectedResult).toContain(governorate2);
      });

      it('should accept null and undefined values', () => {
        const governorate: IGovernorate = sampleWithRequiredData;
        expectedResult = service.addGovernorateToCollectionIfMissing([], null, governorate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(governorate);
      });

      it('should return initial array if no Governorate is added', () => {
        const governorateCollection: IGovernorate[] = [sampleWithRequiredData];
        expectedResult = service.addGovernorateToCollectionIfMissing(governorateCollection, undefined, null);
        expect(expectedResult).toEqual(governorateCollection);
      });
    });

    describe('compareGovernorate', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGovernorate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '06b5bd02-0f85-4d95-9111-578d64b53b5c' };
        const entity2 = null;

        const compareResult1 = service.compareGovernorate(entity1, entity2);
        const compareResult2 = service.compareGovernorate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '06b5bd02-0f85-4d95-9111-578d64b53b5c' };
        const entity2 = { id: '115a502d-2c89-49eb-b046-b41ce5c05608' };

        const compareResult1 = service.compareGovernorate(entity1, entity2);
        const compareResult2 = service.compareGovernorate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: '06b5bd02-0f85-4d95-9111-578d64b53b5c' };
        const entity2 = { id: '06b5bd02-0f85-4d95-9111-578d64b53b5c' };

        const compareResult1 = service.compareGovernorate(entity1, entity2);
        const compareResult2 = service.compareGovernorate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
