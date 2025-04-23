import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInsurancePack } from '../insurance-pack.model';
import { InsurancePackService } from '../service/insurance-pack.service';

const insurancePackResolve = (route: ActivatedRouteSnapshot): Observable<null | IInsurancePack> => {
  const id = route.params.id;
  if (id) {
    return inject(InsurancePackService)
      .find(id)
      .pipe(
        mergeMap((insurancePack: HttpResponse<IInsurancePack>) => {
          if (insurancePack.body) {
            return of(insurancePack.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default insurancePackResolve;
