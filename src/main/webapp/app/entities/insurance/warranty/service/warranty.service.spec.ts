import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IWarranty } from '../warranty.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../warranty.test-samples';

import { WarrantyService } from './warranty.service';

const requireRestSample: IWarranty = {
  ...sampleWithRequiredData,
};

describe('Warranty Service', () => {
  let service: WarrantyService;
  let httpMock: HttpTestingController;
  let expectedResult: IWarranty | IWarranty[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(WarrantyService);
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

    it('should create a Warranty', () => {
      const warranty = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(warranty).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Warranty', () => {
      const warranty = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(warranty).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Warranty', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Warranty', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Warranty', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Warranty', () => {
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

    describe('addWarrantyToCollectionIfMissing', () => {
      it('should add a Warranty to an empty array', () => {
        const warranty: IWarranty = sampleWithRequiredData;
        expectedResult = service.addWarrantyToCollectionIfMissing([], warranty);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(warranty);
      });

      it('should not add a Warranty to an array that contains it', () => {
        const warranty: IWarranty = sampleWithRequiredData;
        const warrantyCollection: IWarranty[] = [
          {
            ...warranty,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWarrantyToCollectionIfMissing(warrantyCollection, warranty);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Warranty to an array that doesn't contain it", () => {
        const warranty: IWarranty = sampleWithRequiredData;
        const warrantyCollection: IWarranty[] = [sampleWithPartialData];
        expectedResult = service.addWarrantyToCollectionIfMissing(warrantyCollection, warranty);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(warranty);
      });

      it('should add only unique Warranty to an array', () => {
        const warrantyArray: IWarranty[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const warrantyCollection: IWarranty[] = [sampleWithRequiredData];
        expectedResult = service.addWarrantyToCollectionIfMissing(warrantyCollection, ...warrantyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const warranty: IWarranty = sampleWithRequiredData;
        const warranty2: IWarranty = sampleWithPartialData;
        expectedResult = service.addWarrantyToCollectionIfMissing([], warranty, warranty2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(warranty);
        expect(expectedResult).toContain(warranty2);
      });

      it('should accept null and undefined values', () => {
        const warranty: IWarranty = sampleWithRequiredData;
        expectedResult = service.addWarrantyToCollectionIfMissing([], null, warranty, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(warranty);
      });

      it('should return initial array if no Warranty is added', () => {
        const warrantyCollection: IWarranty[] = [sampleWithRequiredData];
        expectedResult = service.addWarrantyToCollectionIfMissing(warrantyCollection, undefined, null);
        expect(expectedResult).toEqual(warrantyCollection);
      });
    });

    describe('compareWarranty', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWarranty(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '02209a44-8170-48fc-bfe9-76651f25b708' };
        const entity2 = null;

        const compareResult1 = service.compareWarranty(entity1, entity2);
        const compareResult2 = service.compareWarranty(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '02209a44-8170-48fc-bfe9-76651f25b708' };
        const entity2 = { id: '0e791b1b-70a5-4e8a-8534-6a5596acad14' };

        const compareResult1 = service.compareWarranty(entity1, entity2);
        const compareResult2 = service.compareWarranty(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: '02209a44-8170-48fc-bfe9-76651f25b708' };
        const entity2 = { id: '02209a44-8170-48fc-bfe9-76651f25b708' };

        const compareResult1 = service.compareWarranty(entity1, entity2);
        const compareResult2 = service.compareWarranty(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
