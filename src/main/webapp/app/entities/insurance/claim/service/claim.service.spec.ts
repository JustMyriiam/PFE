import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IClaim } from '../claim.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../claim.test-samples';

import { ClaimService, RestClaim } from './claim.service';

const requireRestSample: RestClaim = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('Claim Service', () => {
  let service: ClaimService;
  let httpMock: HttpTestingController;
  let expectedResult: IClaim | IClaim[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ClaimService);
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

    it('should create a Claim', () => {
      const claim = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(claim).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Claim', () => {
      const claim = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(claim).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Claim', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Claim', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Claim', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Claim', () => {
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

    describe('addClaimToCollectionIfMissing', () => {
      it('should add a Claim to an empty array', () => {
        const claim: IClaim = sampleWithRequiredData;
        expectedResult = service.addClaimToCollectionIfMissing([], claim);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(claim);
      });

      it('should not add a Claim to an array that contains it', () => {
        const claim: IClaim = sampleWithRequiredData;
        const claimCollection: IClaim[] = [
          {
            ...claim,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addClaimToCollectionIfMissing(claimCollection, claim);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Claim to an array that doesn't contain it", () => {
        const claim: IClaim = sampleWithRequiredData;
        const claimCollection: IClaim[] = [sampleWithPartialData];
        expectedResult = service.addClaimToCollectionIfMissing(claimCollection, claim);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(claim);
      });

      it('should add only unique Claim to an array', () => {
        const claimArray: IClaim[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const claimCollection: IClaim[] = [sampleWithRequiredData];
        expectedResult = service.addClaimToCollectionIfMissing(claimCollection, ...claimArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const claim: IClaim = sampleWithRequiredData;
        const claim2: IClaim = sampleWithPartialData;
        expectedResult = service.addClaimToCollectionIfMissing([], claim, claim2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(claim);
        expect(expectedResult).toContain(claim2);
      });

      it('should accept null and undefined values', () => {
        const claim: IClaim = sampleWithRequiredData;
        expectedResult = service.addClaimToCollectionIfMissing([], null, claim, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(claim);
      });

      it('should return initial array if no Claim is added', () => {
        const claimCollection: IClaim[] = [sampleWithRequiredData];
        expectedResult = service.addClaimToCollectionIfMissing(claimCollection, undefined, null);
        expect(expectedResult).toEqual(claimCollection);
      });
    });

    describe('compareClaim', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareClaim(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '94ec77c2-f5a1-41a3-b8f8-7bf9e231e536' };
        const entity2 = null;

        const compareResult1 = service.compareClaim(entity1, entity2);
        const compareResult2 = service.compareClaim(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '94ec77c2-f5a1-41a3-b8f8-7bf9e231e536' };
        const entity2 = { id: '8bb3b970-e511-4c38-8959-e76645f63e0c' };

        const compareResult1 = service.compareClaim(entity1, entity2);
        const compareResult2 = service.compareClaim(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: '94ec77c2-f5a1-41a3-b8f8-7bf9e231e536' };
        const entity2 = { id: '94ec77c2-f5a1-41a3-b8f8-7bf9e231e536' };

        const compareResult1 = service.compareClaim(entity1, entity2);
        const compareResult2 = service.compareClaim(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
