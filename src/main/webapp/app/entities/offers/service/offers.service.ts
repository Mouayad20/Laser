import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOffers, getOffersIdentifier } from '../offers.model';

export type EntityResponseType = HttpResponse<IOffers>;
export type EntityArrayResponseType = HttpResponse<IOffers[]>;

@Injectable({ providedIn: 'root' })
export class OffersService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/offers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(offers: IOffers): Observable<EntityResponseType> {
    return this.http.post<IOffers>(this.resourceUrl, offers, { observe: 'response' });
  }

  update(offers: IOffers): Observable<EntityResponseType> {
    return this.http.put<IOffers>(`${this.resourceUrl}/${getOffersIdentifier(offers) as number}`, offers, { observe: 'response' });
  }

  partialUpdate(offers: IOffers): Observable<EntityResponseType> {
    return this.http.patch<IOffers>(`${this.resourceUrl}/${getOffersIdentifier(offers) as number}`, offers, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOffers>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOffers[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOffersToCollectionIfMissing(offersCollection: IOffers[], ...offersToCheck: (IOffers | null | undefined)[]): IOffers[] {
    const offers: IOffers[] = offersToCheck.filter(isPresent);
    if (offers.length > 0) {
      const offersCollectionIdentifiers = offersCollection.map(offersItem => getOffersIdentifier(offersItem)!);
      const offersToAdd = offers.filter(offersItem => {
        const offersIdentifier = getOffersIdentifier(offersItem);
        if (offersIdentifier == null || offersCollectionIdentifiers.includes(offersIdentifier)) {
          return false;
        }
        offersCollectionIdentifiers.push(offersIdentifier);
        return true;
      });
      return [...offersToAdd, ...offersCollection];
    }
    return offersCollection;
  }
}
