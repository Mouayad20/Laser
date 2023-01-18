import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IShipmentType, getShipmentTypeIdentifier } from '../shipment-type.model';

export type EntityResponseType = HttpResponse<IShipmentType>;
export type EntityArrayResponseType = HttpResponse<IShipmentType[]>;

@Injectable({ providedIn: 'root' })
export class ShipmentTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/shipment-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(shipmentType: IShipmentType): Observable<EntityResponseType> {
    return this.http.post<IShipmentType>(this.resourceUrl, shipmentType, { observe: 'response' });
  }

  update(shipmentType: IShipmentType): Observable<EntityResponseType> {
    return this.http.put<IShipmentType>(`${this.resourceUrl}/${getShipmentTypeIdentifier(shipmentType) as number}`, shipmentType, {
      observe: 'response',
    });
  }

  partialUpdate(shipmentType: IShipmentType): Observable<EntityResponseType> {
    return this.http.patch<IShipmentType>(`${this.resourceUrl}/${getShipmentTypeIdentifier(shipmentType) as number}`, shipmentType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShipmentType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShipmentType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addShipmentTypeToCollectionIfMissing(
    shipmentTypeCollection: IShipmentType[],
    ...shipmentTypesToCheck: (IShipmentType | null | undefined)[]
  ): IShipmentType[] {
    const shipmentTypes: IShipmentType[] = shipmentTypesToCheck.filter(isPresent);
    if (shipmentTypes.length > 0) {
      const shipmentTypeCollectionIdentifiers = shipmentTypeCollection.map(
        shipmentTypeItem => getShipmentTypeIdentifier(shipmentTypeItem)!
      );
      const shipmentTypesToAdd = shipmentTypes.filter(shipmentTypeItem => {
        const shipmentTypeIdentifier = getShipmentTypeIdentifier(shipmentTypeItem);
        if (shipmentTypeIdentifier == null || shipmentTypeCollectionIdentifiers.includes(shipmentTypeIdentifier)) {
          return false;
        }
        shipmentTypeCollectionIdentifiers.push(shipmentTypeIdentifier);
        return true;
      });
      return [...shipmentTypesToAdd, ...shipmentTypeCollection];
    }
    return shipmentTypeCollection;
  }
}
