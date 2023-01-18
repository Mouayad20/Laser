import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserApplication, getUserApplicationIdentifier } from '../user-application.model';

export type EntityResponseType = HttpResponse<IUserApplication>;
export type EntityArrayResponseType = HttpResponse<IUserApplication[]>;

@Injectable({ providedIn: 'root' })
export class UserApplicationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-applications');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userApplication: IUserApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userApplication);
    return this.http
      .post<IUserApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(userApplication: IUserApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userApplication);
    return this.http
      .put<IUserApplication>(`${this.resourceUrl}/${getUserApplicationIdentifier(userApplication) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(userApplication: IUserApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userApplication);
    return this.http
      .patch<IUserApplication>(`${this.resourceUrl}/${getUserApplicationIdentifier(userApplication) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUserApplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUserApplication[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUserApplicationToCollectionIfMissing(
    userApplicationCollection: IUserApplication[],
    ...userApplicationsToCheck: (IUserApplication | null | undefined)[]
  ): IUserApplication[] {
    const userApplications: IUserApplication[] = userApplicationsToCheck.filter(isPresent);
    if (userApplications.length > 0) {
      const userApplicationCollectionIdentifiers = userApplicationCollection.map(
        userApplicationItem => getUserApplicationIdentifier(userApplicationItem)!
      );
      const userApplicationsToAdd = userApplications.filter(userApplicationItem => {
        const userApplicationIdentifier = getUserApplicationIdentifier(userApplicationItem);
        if (userApplicationIdentifier == null || userApplicationCollectionIdentifiers.includes(userApplicationIdentifier)) {
          return false;
        }
        userApplicationCollectionIdentifiers.push(userApplicationIdentifier);
        return true;
      });
      return [...userApplicationsToAdd, ...userApplicationCollection];
    }
    return userApplicationCollection;
  }

  protected convertDateFromClient(userApplication: IUserApplication): IUserApplication {
    return Object.assign({}, userApplication, {
      createdAt: userApplication.createdAt?.isValid() ? userApplication.createdAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((userApplication: IUserApplication) => {
        userApplication.createdAt = userApplication.createdAt ? dayjs(userApplication.createdAt) : undefined;
      });
    }
    return res;
  }
}
