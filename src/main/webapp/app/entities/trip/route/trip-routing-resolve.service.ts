import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrip, Trip } from '../trip.model';
import { TripService } from '../service/trip.service';

@Injectable({ providedIn: 'root' })
export class TripRoutingResolveService implements Resolve<ITrip> {
  constructor(protected service: TripService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrip> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((trip: HttpResponse<Trip>) => {
          if (trip.body) {
            return of(trip.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Trip());
  }
}
