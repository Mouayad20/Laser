import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConstants, getConstantsIdentifier } from '../constants.model';

export type EntityResponseType = HttpResponse<IConstants>;
export type EntityArrayResponseType = HttpResponse<IConstants[]>;

@Injectable({ providedIn: 'root' })
export class ConstantsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/constants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(constants: IConstants): Observable<EntityResponseType> {
    return this.http.post<IConstants>(this.resourceUrl, constants, { observe: 'response' });
  }

  update(constants: IConstants): Observable<EntityResponseType> {
    return this.http.put<IConstants>(`${this.resourceUrl}/${getConstantsIdentifier(constants) as number}`, constants, {
      observe: 'response',
    });
  }

  partialUpdate(constants: IConstants): Observable<EntityResponseType> {
    return this.http.patch<IConstants>(`${this.resourceUrl}/${getConstantsIdentifier(constants) as number}`, constants, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConstants>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConstants[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConstantsToCollectionIfMissing(
    constantsCollection: IConstants[],
    ...constantsToCheck: (IConstants | null | undefined)[]
  ): IConstants[] {
    const constants: IConstants[] = constantsToCheck.filter(isPresent);
    if (constants.length > 0) {
      const constantsCollectionIdentifiers = constantsCollection.map(constantsItem => getConstantsIdentifier(constantsItem)!);
      const constantsToAdd = constants.filter(constantsItem => {
        const constantsIdentifier = getConstantsIdentifier(constantsItem);
        if (constantsIdentifier == null || constantsCollectionIdentifiers.includes(constantsIdentifier)) {
          return false;
        }
        constantsCollectionIdentifiers.push(constantsIdentifier);
        return true;
      });
      return [...constantsToAdd, ...constantsCollection];
    }
    return constantsCollection;
  }
}
