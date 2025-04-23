import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IInsurancePack, NewInsurancePack } from '../insurance-pack.model';

export type PartialUpdateInsurancePack = Partial<IInsurancePack> & Pick<IInsurancePack, 'id'>;

export type EntityResponseType = HttpResponse<IInsurancePack>;
export type EntityArrayResponseType = HttpResponse<IInsurancePack[]>;

@Injectable({ providedIn: 'root' })
export class InsurancePackService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/insurance-packs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/insurance-packs/_search');

  create(insurancePack: NewInsurancePack): Observable<EntityResponseType> {
    return this.http.post<IInsurancePack>(this.resourceUrl, insurancePack, { observe: 'response' });
  }

  update(insurancePack: IInsurancePack): Observable<EntityResponseType> {
    return this.http.put<IInsurancePack>(`${this.resourceUrl}/${this.getInsurancePackIdentifier(insurancePack)}`, insurancePack, {
      observe: 'response',
    });
  }

  partialUpdate(insurancePack: PartialUpdateInsurancePack): Observable<EntityResponseType> {
    return this.http.patch<IInsurancePack>(`${this.resourceUrl}/${this.getInsurancePackIdentifier(insurancePack)}`, insurancePack, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IInsurancePack>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInsurancePack[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInsurancePack[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IInsurancePack[]>()], asapScheduler)));
  }

  getInsurancePackIdentifier(insurancePack: Pick<IInsurancePack, 'id'>): string {
    return insurancePack.id;
  }

  compareInsurancePack(o1: Pick<IInsurancePack, 'id'> | null, o2: Pick<IInsurancePack, 'id'> | null): boolean {
    return o1 && o2 ? this.getInsurancePackIdentifier(o1) === this.getInsurancePackIdentifier(o2) : o1 === o2;
  }

  addInsurancePackToCollectionIfMissing<Type extends Pick<IInsurancePack, 'id'>>(
    insurancePackCollection: Type[],
    ...insurancePacksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const insurancePacks: Type[] = insurancePacksToCheck.filter(isPresent);
    if (insurancePacks.length > 0) {
      const insurancePackCollectionIdentifiers = insurancePackCollection.map(insurancePackItem =>
        this.getInsurancePackIdentifier(insurancePackItem),
      );
      const insurancePacksToAdd = insurancePacks.filter(insurancePackItem => {
        const insurancePackIdentifier = this.getInsurancePackIdentifier(insurancePackItem);
        if (insurancePackCollectionIdentifiers.includes(insurancePackIdentifier)) {
          return false;
        }
        insurancePackCollectionIdentifiers.push(insurancePackIdentifier);
        return true;
      });
      return [...insurancePacksToAdd, ...insurancePackCollection];
    }
    return insurancePackCollection;
  }
}
