import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IClaim, NewClaim } from '../claim.model';

export type PartialUpdateClaim = Partial<IClaim> & Pick<IClaim, 'id'>;

type RestOf<T extends IClaim | NewClaim> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestClaim = RestOf<IClaim>;

export type NewRestClaim = RestOf<NewClaim>;

export type PartialUpdateRestClaim = RestOf<PartialUpdateClaim>;

export type EntityResponseType = HttpResponse<IClaim>;
export type EntityArrayResponseType = HttpResponse<IClaim[]>;

@Injectable({ providedIn: 'root' })
export class ClaimService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/claims');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/claims/_search');

  create(claim: NewClaim): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(claim);
    return this.http.post<RestClaim>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(claim: IClaim): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(claim);
    return this.http
      .put<RestClaim>(`${this.resourceUrl}/${this.getClaimIdentifier(claim)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(claim: PartialUpdateClaim): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(claim);
    return this.http
      .patch<RestClaim>(`${this.resourceUrl}/${this.getClaimIdentifier(claim)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestClaim>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestClaim[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestClaim[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IClaim[]>()], asapScheduler)),
    );
  }

  getClaimIdentifier(claim: Pick<IClaim, 'id'>): string {
    return claim.id;
  }

  compareClaim(o1: Pick<IClaim, 'id'> | null, o2: Pick<IClaim, 'id'> | null): boolean {
    return o1 && o2 ? this.getClaimIdentifier(o1) === this.getClaimIdentifier(o2) : o1 === o2;
  }

  addClaimToCollectionIfMissing<Type extends Pick<IClaim, 'id'>>(
    claimCollection: Type[],
    ...claimsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const claims: Type[] = claimsToCheck.filter(isPresent);
    if (claims.length > 0) {
      const claimCollectionIdentifiers = claimCollection.map(claimItem => this.getClaimIdentifier(claimItem));
      const claimsToAdd = claims.filter(claimItem => {
        const claimIdentifier = this.getClaimIdentifier(claimItem);
        if (claimCollectionIdentifiers.includes(claimIdentifier)) {
          return false;
        }
        claimCollectionIdentifiers.push(claimIdentifier);
        return true;
      });
      return [...claimsToAdd, ...claimCollection];
    }
    return claimCollection;
  }

  protected convertDateFromClient<T extends IClaim | NewClaim | PartialUpdateClaim>(claim: T): RestOf<T> {
    return {
      ...claim,
      date: claim.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restClaim: RestClaim): IClaim {
    return {
      ...restClaim,
      date: restClaim.date ? dayjs(restClaim.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestClaim>): HttpResponse<IClaim> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestClaim[]>): HttpResponse<IClaim[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
