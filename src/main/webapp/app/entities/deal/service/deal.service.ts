import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeal, getDealIdentifier } from '../deal.model';

export type EntityResponseType = HttpResponse<IDeal>;
export type EntityArrayResponseType = HttpResponse<IDeal[]>;

@Injectable({ providedIn: 'root' })
export class DealService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/deals');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deal: IDeal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deal);
    return this.http
      .post<IDeal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(deal: IDeal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deal);
    return this.http
      .put<IDeal>(`${this.resourceUrl}/${getDealIdentifier(deal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(deal: IDeal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deal);
    return this.http
      .patch<IDeal>(`${this.resourceUrl}/${getDealIdentifier(deal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDeal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDeal[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDealToCollectionIfMissing(dealCollection: IDeal[], ...dealsToCheck: (IDeal | null | undefined)[]): IDeal[] {
    const deals: IDeal[] = dealsToCheck.filter(isPresent);
    if (deals.length > 0) {
      const dealCollectionIdentifiers = dealCollection.map(dealItem => getDealIdentifier(dealItem)!);
      const dealsToAdd = deals.filter(dealItem => {
        const dealIdentifier = getDealIdentifier(dealItem);
        if (dealIdentifier == null || dealCollectionIdentifiers.includes(dealIdentifier)) {
          return false;
        }
        dealCollectionIdentifiers.push(dealIdentifier);
        return true;
      });
      return [...dealsToAdd, ...dealCollection];
    }
    return dealCollection;
  }

  protected convertDateFromClient(deal: IDeal): IDeal {
    return Object.assign({}, deal, {
      arrivelDate: deal.arrivelDate?.isValid() ? deal.arrivelDate.toJSON() : undefined,
      expectedDate: deal.expectedDate?.isValid() ? deal.expectedDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.arrivelDate = res.body.arrivelDate ? dayjs(res.body.arrivelDate) : undefined;
      res.body.expectedDate = res.body.expectedDate ? dayjs(res.body.expectedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((deal: IDeal) => {
        deal.arrivelDate = deal.arrivelDate ? dayjs(deal.arrivelDate) : undefined;
        deal.expectedDate = deal.expectedDate ? dayjs(deal.expectedDate) : undefined;
      });
    }
    return res;
  }
}
