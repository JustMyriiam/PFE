import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClaim } from '../claim.model';
import { ClaimService } from '../service/claim.service';

const claimResolve = (route: ActivatedRouteSnapshot): Observable<null | IClaim> => {
  const id = route.params.id;
  if (id) {
    return inject(ClaimService)
      .find(id)
      .pipe(
        mergeMap((claim: HttpResponse<IClaim>) => {
          if (claim.body) {
            return of(claim.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default claimResolve;
