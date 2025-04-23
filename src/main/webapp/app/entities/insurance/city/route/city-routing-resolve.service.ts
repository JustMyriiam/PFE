import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICity } from '../city.model';
import { CityService } from '../service/city.service';

const cityResolve = (route: ActivatedRouteSnapshot): Observable<null | ICity> => {
  const id = route.params.id;
  if (id) {
    return inject(CityService)
      .find(id)
      .pipe(
        mergeMap((city: HttpResponse<ICity>) => {
          if (city.body) {
            return of(city.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default cityResolve;
