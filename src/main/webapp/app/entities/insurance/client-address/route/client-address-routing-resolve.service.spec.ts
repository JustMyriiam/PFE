import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IClientAddress } from '../client-address.model';
import { ClientAddressService } from '../service/client-address.service';

import clientAddressResolve from './client-address-routing-resolve.service';

describe('ClientAddress routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ClientAddressService;
  let resultClientAddress: IClientAddress | null | undefined;

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
    service = TestBed.inject(ClientAddressService);
    resultClientAddress = undefined;
  });

  describe('resolve', () => {
    it('should return IClientAddress returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        clientAddressResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultClientAddress = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith('9fec3727-3421-4967-b213-ba36557ca194');
      expect(resultClientAddress).toEqual({ id: '9fec3727-3421-4967-b213-ba36557ca194' });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        clientAddressResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultClientAddress = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toHaveBeenCalled();
      expect(resultClientAddress).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IClientAddress>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        clientAddressResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultClientAddress = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith('9fec3727-3421-4967-b213-ba36557ca194');
      expect(resultClientAddress).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
