import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConstants, Constants } from '../constants.model';
import { ConstantsService } from '../service/constants.service';

@Injectable({ providedIn: 'root' })
export class ConstantsRoutingResolveService implements Resolve<IConstants> {
  constructor(protected service: ConstantsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConstants> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((constants: HttpResponse<Constants>) => {
          if (constants.body) {
            return of(constants.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Constants());
  }
}
