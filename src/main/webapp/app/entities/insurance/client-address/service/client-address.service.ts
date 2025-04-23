import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IClientAddress, NewClientAddress } from '../client-address.model';

export type PartialUpdateClientAddress = Partial<IClientAddress> & Pick<IClientAddress, 'id'>;

export type EntityResponseType = HttpResponse<IClientAddress>;
export type EntityArrayResponseType = HttpResponse<IClientAddress[]>;

@Injectable({ providedIn: 'root' })
export class ClientAddressService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/client-addresses');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/client-addresses/_search');

  create(clientAddress: NewClientAddress): Observable<EntityResponseType> {
    return this.http.post<IClientAddress>(this.resourceUrl, clientAddress, { observe: 'response' });
  }

  update(clientAddress: IClientAddress): Observable<EntityResponseType> {
    return this.http.put<IClientAddress>(`${this.resourceUrl}/${this.getClientAddressIdentifier(clientAddress)}`, clientAddress, {
      observe: 'response',
    });
  }

  partialUpdate(clientAddress: PartialUpdateClientAddress): Observable<EntityResponseType> {
    return this.http.patch<IClientAddress>(`${this.resourceUrl}/${this.getClientAddressIdentifier(clientAddress)}`, clientAddress, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IClientAddress>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClientAddress[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClientAddress[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IClientAddress[]>()], asapScheduler)));
  }

  getClientAddressIdentifier(clientAddress: Pick<IClientAddress, 'id'>): string {
    return clientAddress.id;
  }

  compareClientAddress(o1: Pick<IClientAddress, 'id'> | null, o2: Pick<IClientAddress, 'id'> | null): boolean {
    return o1 && o2 ? this.getClientAddressIdentifier(o1) === this.getClientAddressIdentifier(o2) : o1 === o2;
  }

  addClientAddressToCollectionIfMissing<Type extends Pick<IClientAddress, 'id'>>(
    clientAddressCollection: Type[],
    ...clientAddressesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const clientAddresses: Type[] = clientAddressesToCheck.filter(isPresent);
    if (clientAddresses.length > 0) {
      const clientAddressCollectionIdentifiers = clientAddressCollection.map(clientAddressItem =>
        this.getClientAddressIdentifier(clientAddressItem),
      );
      const clientAddressesToAdd = clientAddresses.filter(clientAddressItem => {
        const clientAddressIdentifier = this.getClientAddressIdentifier(clientAddressItem);
        if (clientAddressCollectionIdentifiers.includes(clientAddressIdentifier)) {
          return false;
        }
        clientAddressCollectionIdentifiers.push(clientAddressIdentifier);
        return true;
      });
      return [...clientAddressesToAdd, ...clientAddressCollection];
    }
    return clientAddressCollection;
  }
}
