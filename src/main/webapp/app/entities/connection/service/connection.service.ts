import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConnection, getConnectionIdentifier } from '../connection.model';

export type EntityResponseType = HttpResponse<IConnection>;
export type EntityArrayResponseType = HttpResponse<IConnection[]>;

@Injectable({ providedIn: 'root' })
export class ConnectionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/connections');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(connection: IConnection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(connection);
    return this.http
      .post<IConnection>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(connection: IConnection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(connection);
    return this.http
      .put<IConnection>(`${this.resourceUrl}/${getConnectionIdentifier(connection) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(connection: IConnection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(connection);
    return this.http
      .patch<IConnection>(`${this.resourceUrl}/${getConnectionIdentifier(connection) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IConnection>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IConnection[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConnectionToCollectionIfMissing(
    connectionCollection: IConnection[],
    ...connectionsToCheck: (IConnection | null | undefined)[]
  ): IConnection[] {
    const connections: IConnection[] = connectionsToCheck.filter(isPresent);
    if (connections.length > 0) {
      const connectionCollectionIdentifiers = connectionCollection.map(connectionItem => getConnectionIdentifier(connectionItem)!);
      const connectionsToAdd = connections.filter(connectionItem => {
        const connectionIdentifier = getConnectionIdentifier(connectionItem);
        if (connectionIdentifier == null || connectionCollectionIdentifiers.includes(connectionIdentifier)) {
          return false;
        }
        connectionCollectionIdentifiers.push(connectionIdentifier);
        return true;
      });
      return [...connectionsToAdd, ...connectionCollection];
    }
    return connectionCollection;
  }

  protected convertDateFromClient(connection: IConnection): IConnection {
    return Object.assign({}, connection, {
      localTokenExpiryDate: connection.localTokenExpiryDate?.isValid() ? connection.localTokenExpiryDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.localTokenExpiryDate = res.body.localTokenExpiryDate ? dayjs(res.body.localTokenExpiryDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((connection: IConnection) => {
        connection.localTokenExpiryDate = connection.localTokenExpiryDate ? dayjs(connection.localTokenExpiryDate) : undefined;
      });
    }
    return res;
  }
}
