import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWarranty } from '../warranty.model';
import { WarrantyService } from '../service/warranty.service';

const warrantyResolve = (route: ActivatedRouteSnapshot): Observable<null | IWarranty> => {
  const id = route.params.id;
  if (id) {
    return inject(WarrantyService)
      .find(id)
      .pipe(
        mergeMap((warranty: HttpResponse<IWarranty>) => {
          if (warranty.body) {
            return of(warranty.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default warrantyResolve;
