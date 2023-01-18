import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserApplication, UserApplication } from '../user-application.model';
import { UserApplicationService } from '../service/user-application.service';

@Injectable({ providedIn: 'root' })
export class UserApplicationRoutingResolveService implements Resolve<IUserApplication> {
  constructor(protected service: UserApplicationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserApplication> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userApplication: HttpResponse<UserApplication>) => {
          if (userApplication.body) {
            return of(userApplication.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserApplication());
  }
}
