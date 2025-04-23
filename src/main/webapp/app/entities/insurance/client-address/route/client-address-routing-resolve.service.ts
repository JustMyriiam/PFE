import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClientAddress } from '../client-address.model';
import { ClientAddressService } from '../service/client-address.service';

const clientAddressResolve = (route: ActivatedRouteSnapshot): Observable<null | IClientAddress> => {
  const id = route.params.id;
  if (id) {
    return inject(ClientAddressService)
      .find(id)
      .pipe(
        mergeMap((clientAddress: HttpResponse<IClientAddress>) => {
          if (clientAddress.body) {
            return of(clientAddress.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default clientAddressResolve;
