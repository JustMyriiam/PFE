import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IWarranty, NewWarranty } from '../warranty.model';

export type PartialUpdateWarranty = Partial<IWarranty> & Pick<IWarranty, 'id'>;

export type EntityResponseType = HttpResponse<IWarranty>;
export type EntityArrayResponseType = HttpResponse<IWarranty[]>;

@Injectable({ providedIn: 'root' })
export class WarrantyService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/warranties');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/warranties/_search');

  create(warranty: NewWarranty): Observable<EntityResponseType> {
    return this.http.post<IWarranty>(this.resourceUrl, warranty, { observe: 'response' });
  }

  update(warranty: IWarranty): Observable<EntityResponseType> {
    return this.http.put<IWarranty>(`${this.resourceUrl}/${this.getWarrantyIdentifier(warranty)}`, warranty, { observe: 'response' });
  }

  partialUpdate(warranty: PartialUpdateWarranty): Observable<EntityResponseType> {
    return this.http.patch<IWarranty>(`${this.resourceUrl}/${this.getWarrantyIdentifier(warranty)}`, warranty, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IWarranty>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWarranty[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWarranty[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IWarranty[]>()], asapScheduler)));
  }

  getWarrantyIdentifier(warranty: Pick<IWarranty, 'id'>): string {
    return warranty.id;
  }

  compareWarranty(o1: Pick<IWarranty, 'id'> | null, o2: Pick<IWarranty, 'id'> | null): boolean {
    return o1 && o2 ? this.getWarrantyIdentifier(o1) === this.getWarrantyIdentifier(o2) : o1 === o2;
  }

  addWarrantyToCollectionIfMissing<Type extends Pick<IWarranty, 'id'>>(
    warrantyCollection: Type[],
    ...warrantiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const warranties: Type[] = warrantiesToCheck.filter(isPresent);
    if (warranties.length > 0) {
      const warrantyCollectionIdentifiers = warrantyCollection.map(warrantyItem => this.getWarrantyIdentifier(warrantyItem));
      const warrantiesToAdd = warranties.filter(warrantyItem => {
        const warrantyIdentifier = this.getWarrantyIdentifier(warrantyItem);
        if (warrantyCollectionIdentifiers.includes(warrantyIdentifier)) {
          return false;
        }
        warrantyCollectionIdentifiers.push(warrantyIdentifier);
        return true;
      });
      return [...warrantiesToAdd, ...warrantyCollection];
    }
    return warrantyCollection;
  }
}
