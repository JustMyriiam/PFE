import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IWarranty } from '../warranty.model';
import { WarrantyService } from '../service/warranty.service';

import warrantyResolve from './warranty-routing-resolve.service';

describe('Warranty routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: WarrantyService;
  let resultWarranty: IWarranty | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(WarrantyService);
    resultWarranty = undefined;
  });

  describe('resolve', () => {
    it('should return IWarranty returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        warrantyResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultWarranty = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith('9fec3727-3421-4967-b213-ba36557ca194');
      expect(resultWarranty).toEqual({ id: '9fec3727-3421-4967-b213-ba36557ca194' });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        warrantyResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultWarranty = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toHaveBeenCalled();
      expect(resultWarranty).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IWarranty>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        warrantyResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultWarranty = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith('9fec3727-3421-4967-b213-ba36557ca194');
      expect(resultWarranty).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
