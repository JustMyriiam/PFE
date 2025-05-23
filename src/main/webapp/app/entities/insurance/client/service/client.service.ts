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
import { IClient, NewClient } from '../client.model';

export type PartialUpdateClient = Partial<IClient> & Pick<IClient, 'id'>;

type RestOf<T extends IClient | NewClient> = Omit<
  T,
  'identityEmissionDate' | 'birthDate' | 'identityIssueDate' | 'drivingLicenseIssueDate'
> & {
  identityEmissionDate?: string | null;
  birthDate?: string | null;
  identityIssueDate?: string | null;
  drivingLicenseIssueDate?: string | null;
};

export type RestClient = RestOf<IClient>;

export type NewRestClient = RestOf<NewClient>;

export type PartialUpdateRestClient = RestOf<PartialUpdateClient>;

export type EntityResponseType = HttpResponse<IClient>;
export type EntityArrayResponseType = HttpResponse<IClient[]>;

@Injectable({ providedIn: 'root' })
export class ClientService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/clients');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/clients/_search');

  create(client: NewClient): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(client);
    return this.http
      .post<RestClient>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(client: IClient): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(client);
    return this.http
      .put<RestClient>(`${this.resourceUrl}/${this.getClientIdentifier(client)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(client: PartialUpdateClient): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(client);
    return this.http
      .patch<RestClient>(`${this.resourceUrl}/${this.getClientIdentifier(client)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestClient>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestClient[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestClient[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IClient[]>()], asapScheduler)),
    );
  }

  getClientIdentifier(client: Pick<IClient, 'id'>): string {
    return client.id;
  }

  compareClient(o1: Pick<IClient, 'id'> | null, o2: Pick<IClient, 'id'> | null): boolean {
    return o1 && o2 ? this.getClientIdentifier(o1) === this.getClientIdentifier(o2) : o1 === o2;
  }

  addClientToCollectionIfMissing<Type extends Pick<IClient, 'id'>>(
    clientCollection: Type[],
    ...clientsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const clients: Type[] = clientsToCheck.filter(isPresent);
    if (clients.length > 0) {
      const clientCollectionIdentifiers = clientCollection.map(clientItem => this.getClientIdentifier(clientItem));
      const clientsToAdd = clients.filter(clientItem => {
        const clientIdentifier = this.getClientIdentifier(clientItem);
        if (clientCollectionIdentifiers.includes(clientIdentifier)) {
          return false;
        }
        clientCollectionIdentifiers.push(clientIdentifier);
        return true;
      });
      return [...clientsToAdd, ...clientCollection];
    }
    return clientCollection;
  }

  protected convertDateFromClient<T extends IClient | NewClient | PartialUpdateClient>(client: T): RestOf<T> {
    return {
      ...client,
      identityEmissionDate: client.identityEmissionDate?.format(DATE_FORMAT) ?? null,
      birthDate: client.birthDate?.format(DATE_FORMAT) ?? null,
      identityIssueDate: client.identityIssueDate?.format(DATE_FORMAT) ?? null,
      drivingLicenseIssueDate: client.drivingLicenseIssueDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restClient: RestClient): IClient {
    return {
      ...restClient,
      identityEmissionDate: restClient.identityEmissionDate ? dayjs(restClient.identityEmissionDate) : undefined,
      birthDate: restClient.birthDate ? dayjs(restClient.birthDate) : undefined,
      identityIssueDate: restClient.identityIssueDate ? dayjs(restClient.identityIssueDate) : undefined,
      drivingLicenseIssueDate: restClient.drivingLicenseIssueDate ? dayjs(restClient.drivingLicenseIssueDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestClient>): HttpResponse<IClient> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestClient[]>): HttpResponse<IClient[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
