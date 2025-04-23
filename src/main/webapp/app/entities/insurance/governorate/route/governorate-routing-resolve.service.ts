import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGovernorate } from '../governorate.model';
import { GovernorateService } from '../service/governorate.service';

const governorateResolve = (route: ActivatedRouteSnapshot): Observable<null | IGovernorate> => {
  const id = route.params.id;
  if (id) {
    return inject(GovernorateService)
      .find(id)
      .pipe(
        mergeMap((governorate: HttpResponse<IGovernorate>) => {
          if (governorate.body) {
            return of(governorate.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default governorateResolve;
