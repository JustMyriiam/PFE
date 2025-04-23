import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAgency, NewAgency } from '../agency.model';

export type PartialUpdateAgency = Partial<IAgency> & Pick<IAgency, 'id'>;

export type EntityResponseType = HttpResponse<IAgency>;
export type EntityArrayResponseType = HttpResponse<IAgency[]>;

@Injectable({ providedIn: 'root' })
export class AgencyService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agencies');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/agencies/_search');

  create(agency: NewAgency): Observable<EntityResponseType> {
    return this.http.post<IAgency>(this.resourceUrl, agency, { observe: 'response' });
  }

  update(agency: IAgency): Observable<EntityResponseType> {
    return this.http.put<IAgency>(`${this.resourceUrl}/${this.getAgencyIdentifier(agency)}`, agency, { observe: 'response' });
  }

  partialUpdate(agency: PartialUpdateAgency): Observable<EntityResponseType> {
    return this.http.patch<IAgency>(`${this.resourceUrl}/${this.getAgencyIdentifier(agency)}`, agency, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAgency>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAgency[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAgency[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IAgency[]>()], asapScheduler)));
  }

  getAgencyIdentifier(agency: Pick<IAgency, 'id'>): string {
    return agency.id;
  }

  compareAgency(o1: Pick<IAgency, 'id'> | null, o2: Pick<IAgency, 'id'> | null): boolean {
    return o1 && o2 ? this.getAgencyIdentifier(o1) === this.getAgencyIdentifier(o2) : o1 === o2;
  }

  addAgencyToCollectionIfMissing<Type extends Pick<IAgency, 'id'>>(
    agencyCollection: Type[],
    ...agenciesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const agencies: Type[] = agenciesToCheck.filter(isPresent);
    if (agencies.length > 0) {
      const agencyCollectionIdentifiers = agencyCollection.map(agencyItem => this.getAgencyIdentifier(agencyItem));
      const agenciesToAdd = agencies.filter(agencyItem => {
        const agencyIdentifier = this.getAgencyIdentifier(agencyItem);
        if (agencyCollectionIdentifiers.includes(agencyIdentifier)) {
          return false;
        }
        agencyCollectionIdentifiers.push(agencyIdentifier);
        return true;
      });
      return [...agenciesToAdd, ...agencyCollection];
    }
    return agencyCollection;
  }
}
