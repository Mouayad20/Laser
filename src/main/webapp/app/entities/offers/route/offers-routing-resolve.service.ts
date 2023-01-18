import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOffers, Offers } from '../offers.model';
import { OffersService } from '../service/offers.service';

@Injectable({ providedIn: 'root' })
export class OffersRoutingResolveService implements Resolve<IOffers> {
  constructor(protected service: OffersService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOffers> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((offers: HttpResponse<Offers>) => {
          if (offers.body) {
            return of(offers.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Offers());
  }
}
