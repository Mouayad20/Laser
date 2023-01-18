import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICountries, getCountriesIdentifier } from '../countries.model';

export type EntityResponseType = HttpResponse<ICountries>;
export type EntityArrayResponseType = HttpResponse<ICountries[]>;

@Injectable({ providedIn: 'root' })
export class CountriesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/countries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(countries: ICountries): Observable<EntityResponseType> {
    return this.http.post<ICountries>(this.resourceUrl, countries, { observe: 'response' });
  }

  update(countries: ICountries): Observable<EntityResponseType> {
    return this.http.put<ICountries>(`${this.resourceUrl}/${getCountriesIdentifier(countries) as number}`, countries, {
      observe: 'response',
    });
  }

  partialUpdate(countries: ICountries): Observable<EntityResponseType> {
    return this.http.patch<ICountries>(`${this.resourceUrl}/${getCountriesIdentifier(countries) as number}`, countries, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICountries>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICountries[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCountriesToCollectionIfMissing(
    countriesCollection: ICountries[],
    ...countriesToCheck: (ICountries | null | undefined)[]
  ): ICountries[] {
    const countries: ICountries[] = countriesToCheck.filter(isPresent);
    if (countries.length > 0) {
      const countriesCollectionIdentifiers = countriesCollection.map(countriesItem => getCountriesIdentifier(countriesItem)!);
      const countriesToAdd = countries.filter(countriesItem => {
        const countriesIdentifier = getCountriesIdentifier(countriesItem);
        if (countriesIdentifier == null || countriesCollectionIdentifiers.includes(countriesIdentifier)) {
          return false;
        }
        countriesCollectionIdentifiers.push(countriesIdentifier);
        return true;
      });
      return [...countriesToAdd, ...countriesCollection];
    }
    return countriesCollection;
  }
}
