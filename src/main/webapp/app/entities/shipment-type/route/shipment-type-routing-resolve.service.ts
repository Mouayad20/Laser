import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IShipmentType, ShipmentType } from '../shipment-type.model';
import { ShipmentTypeService } from '../service/shipment-type.service';

@Injectable({ providedIn: 'root' })
export class ShipmentTypeRoutingResolveService implements Resolve<IShipmentType> {
  constructor(protected service: ShipmentTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IShipmentType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((shipmentType: HttpResponse<ShipmentType>) => {
          if (shipmentType.body) {
            return of(shipmentType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ShipmentType());
  }
}
