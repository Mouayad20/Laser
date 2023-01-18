import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDealStatus, DealStatus } from '../deal-status.model';
import { DealStatusService } from '../service/deal-status.service';

@Injectable({ providedIn: 'root' })
export class DealStatusRoutingResolveService implements Resolve<IDealStatus> {
  constructor(protected service: DealStatusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDealStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dealStatus: HttpResponse<DealStatus>) => {
          if (dealStatus.body) {
            return of(dealStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DealStatus());
  }
}
