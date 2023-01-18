import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAccountProvider, getAccountProviderIdentifier } from '../account-provider.model';

export type EntityResponseType = HttpResponse<IAccountProvider>;
export type EntityArrayResponseType = HttpResponse<IAccountProvider[]>;

@Injectable({ providedIn: 'root' })
export class AccountProviderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-providers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(accountProvider: IAccountProvider): Observable<EntityResponseType> {
    return this.http.post<IAccountProvider>(this.resourceUrl, accountProvider, { observe: 'response' });
  }

  update(accountProvider: IAccountProvider): Observable<EntityResponseType> {
    return this.http.put<IAccountProvider>(
      `${this.resourceUrl}/${getAccountProviderIdentifier(accountProvider) as number}`,
      accountProvider,
      { observe: 'response' }
    );
  }

  partialUpdate(accountProvider: IAccountProvider): Observable<EntityResponseType> {
    return this.http.patch<IAccountProvider>(
      `${this.resourceUrl}/${getAccountProviderIdentifier(accountProvider) as number}`,
      accountProvider,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccountProvider>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccountProvider[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAccountProviderToCollectionIfMissing(
    accountProviderCollection: IAccountProvider[],
    ...accountProvidersToCheck: (IAccountProvider | null | undefined)[]
  ): IAccountProvider[] {
    const accountProviders: IAccountProvider[] = accountProvidersToCheck.filter(isPresent);
    if (accountProviders.length > 0) {
      const accountProviderCollectionIdentifiers = accountProviderCollection.map(
        accountProviderItem => getAccountProviderIdentifier(accountProviderItem)!
      );
      const accountProvidersToAdd = accountProviders.filter(accountProviderItem => {
        const accountProviderIdentifier = getAccountProviderIdentifier(accountProviderItem);
        if (accountProviderIdentifier == null || accountProviderCollectionIdentifiers.includes(accountProviderIdentifier)) {
          return false;
        }
        accountProviderCollectionIdentifiers.push(accountProviderIdentifier);
        return true;
      });
      return [...accountProvidersToAdd, ...accountProviderCollection];
    }
    return accountProviderCollection;
  }
}
