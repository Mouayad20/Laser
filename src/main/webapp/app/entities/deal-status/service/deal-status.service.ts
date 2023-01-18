import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDealStatus, getDealStatusIdentifier } from '../deal-status.model';

export type EntityResponseType = HttpResponse<IDealStatus>;
export type EntityArrayResponseType = HttpResponse<IDealStatus[]>;

@Injectable({ providedIn: 'root' })
export class DealStatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/deal-statuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dealStatus: IDealStatus): Observable<EntityResponseType> {
    return this.http.post<IDealStatus>(this.resourceUrl, dealStatus, { observe: 'response' });
  }

  update(dealStatus: IDealStatus): Observable<EntityResponseType> {
    return this.http.put<IDealStatus>(`${this.resourceUrl}/${getDealStatusIdentifier(dealStatus) as number}`, dealStatus, {
      observe: 'response',
    });
  }

  partialUpdate(dealStatus: IDealStatus): Observable<EntityResponseType> {
    return this.http.patch<IDealStatus>(`${this.resourceUrl}/${getDealStatusIdentifier(dealStatus) as number}`, dealStatus, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDealStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDealStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDealStatusToCollectionIfMissing(
    dealStatusCollection: IDealStatus[],
    ...dealStatusesToCheck: (IDealStatus | null | undefined)[]
  ): IDealStatus[] {
    const dealStatuses: IDealStatus[] = dealStatusesToCheck.filter(isPresent);
    if (dealStatuses.length > 0) {
      const dealStatusCollectionIdentifiers = dealStatusCollection.map(dealStatusItem => getDealStatusIdentifier(dealStatusItem)!);
      const dealStatusesToAdd = dealStatuses.filter(dealStatusItem => {
        const dealStatusIdentifier = getDealStatusIdentifier(dealStatusItem);
        if (dealStatusIdentifier == null || dealStatusCollectionIdentifiers.includes(dealStatusIdentifier)) {
          return false;
        }
        dealStatusCollectionIdentifiers.push(dealStatusIdentifier);
        return true;
      });
      return [...dealStatusesToAdd, ...dealStatusCollection];
    }
    return dealStatusCollection;
  }
}
