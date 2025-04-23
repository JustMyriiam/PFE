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
import { IVehicle, NewVehicle } from '../vehicle.model';

export type PartialUpdateVehicle = Partial<IVehicle> & Pick<IVehicle, 'id'>;

type RestOf<T extends IVehicle | NewVehicle> = Omit<T, 'firstRegistrationDate' | 'expirationDate'> & {
  firstRegistrationDate?: string | null;
  expirationDate?: string | null;
};

export type RestVehicle = RestOf<IVehicle>;

export type NewRestVehicle = RestOf<NewVehicle>;

export type PartialUpdateRestVehicle = RestOf<PartialUpdateVehicle>;

export type EntityResponseType = HttpResponse<IVehicle>;
export type EntityArrayResponseType = HttpResponse<IVehicle[]>;

@Injectable({ providedIn: 'root' })
export class VehicleService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicles');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/vehicles/_search');

  create(vehicle: NewVehicle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicle);
    return this.http
      .post<RestVehicle>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(vehicle: IVehicle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicle);
    return this.http
      .put<RestVehicle>(`${this.resourceUrl}/${this.getVehicleIdentifier(vehicle)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(vehicle: PartialUpdateVehicle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicle);
    return this.http
      .patch<RestVehicle>(`${this.resourceUrl}/${this.getVehicleIdentifier(vehicle)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestVehicle>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVehicle[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestVehicle[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IVehicle[]>()], asapScheduler)),
    );
  }

  getVehicleIdentifier(vehicle: Pick<IVehicle, 'id'>): string {
    return vehicle.id;
  }

  compareVehicle(o1: Pick<IVehicle, 'id'> | null, o2: Pick<IVehicle, 'id'> | null): boolean {
    return o1 && o2 ? this.getVehicleIdentifier(o1) === this.getVehicleIdentifier(o2) : o1 === o2;
  }

  addVehicleToCollectionIfMissing<Type extends Pick<IVehicle, 'id'>>(
    vehicleCollection: Type[],
    ...vehiclesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vehicles: Type[] = vehiclesToCheck.filter(isPresent);
    if (vehicles.length > 0) {
      const vehicleCollectionIdentifiers = vehicleCollection.map(vehicleItem => this.getVehicleIdentifier(vehicleItem));
      const vehiclesToAdd = vehicles.filter(vehicleItem => {
        const vehicleIdentifier = this.getVehicleIdentifier(vehicleItem);
        if (vehicleCollectionIdentifiers.includes(vehicleIdentifier)) {
          return false;
        }
        vehicleCollectionIdentifiers.push(vehicleIdentifier);
        return true;
      });
      return [...vehiclesToAdd, ...vehicleCollection];
    }
    return vehicleCollection;
  }

  protected convertDateFromClient<T extends IVehicle | NewVehicle | PartialUpdateVehicle>(vehicle: T): RestOf<T> {
    return {
      ...vehicle,
      firstRegistrationDate: vehicle.firstRegistrationDate?.format(DATE_FORMAT) ?? null,
      expirationDate: vehicle.expirationDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restVehicle: RestVehicle): IVehicle {
    return {
      ...restVehicle,
      firstRegistrationDate: restVehicle.firstRegistrationDate ? dayjs(restVehicle.firstRegistrationDate) : undefined,
      expirationDate: restVehicle.expirationDate ? dayjs(restVehicle.expirationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVehicle>): HttpResponse<IVehicle> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVehicle[]>): HttpResponse<IVehicle[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
