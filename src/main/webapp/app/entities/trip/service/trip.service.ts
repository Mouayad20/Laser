import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrip, getTripIdentifier } from '../trip.model';

export type EntityResponseType = HttpResponse<ITrip>;
export type EntityArrayResponseType = HttpResponse<ITrip[]>;

@Injectable({ providedIn: 'root' })
export class TripService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trips');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(trip: ITrip): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trip);
    return this.http
      .post<ITrip>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(trip: ITrip): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trip);
    return this.http
      .put<ITrip>(`${this.resourceUrl}/${getTripIdentifier(trip) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(trip: ITrip): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trip);
    return this.http
      .patch<ITrip>(`${this.resourceUrl}/${getTripIdentifier(trip) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITrip>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITrip[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTripToCollectionIfMissing(tripCollection: ITrip[], ...tripsToCheck: (ITrip | null | undefined)[]): ITrip[] {
    const trips: ITrip[] = tripsToCheck.filter(isPresent);
    if (trips.length > 0) {
      const tripCollectionIdentifiers = tripCollection.map(tripItem => getTripIdentifier(tripItem)!);
      const tripsToAdd = trips.filter(tripItem => {
        const tripIdentifier = getTripIdentifier(tripItem);
        if (tripIdentifier == null || tripCollectionIdentifiers.includes(tripIdentifier)) {
          return false;
        }
        tripCollectionIdentifiers.push(tripIdentifier);
        return true;
      });
      return [...tripsToAdd, ...tripCollection];
    }
    return tripCollection;
  }

  protected convertDateFromClient(trip: ITrip): ITrip {
    return Object.assign({}, trip, {
      createdAt: trip.createdAt?.isValid() ? trip.createdAt.toJSON() : undefined,
      flyTime: trip.flyTime?.isValid() ? trip.flyTime.toJSON() : undefined,
      arriveTime: trip.arriveTime?.isValid() ? trip.arriveTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.flyTime = res.body.flyTime ? dayjs(res.body.flyTime) : undefined;
      res.body.arriveTime = res.body.arriveTime ? dayjs(res.body.arriveTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((trip: ITrip) => {
        trip.createdAt = trip.createdAt ? dayjs(trip.createdAt) : undefined;
        trip.flyTime = trip.flyTime ? dayjs(trip.flyTime) : undefined;
        trip.arriveTime = trip.arriveTime ? dayjs(trip.arriveTime) : undefined;
      });
    }
    return res;
  }
}
