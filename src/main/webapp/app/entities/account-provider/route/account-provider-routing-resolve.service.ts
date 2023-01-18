import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccountProvider, AccountProvider } from '../account-provider.model';
import { AccountProviderService } from '../service/account-provider.service';

@Injectable({ providedIn: 'root' })
export class AccountProviderRoutingResolveService implements Resolve<IAccountProvider> {
  constructor(protected service: AccountProviderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAccountProvider> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((accountProvider: HttpResponse<AccountProvider>) => {
          if (accountProvider.body) {
            return of(accountProvider.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AccountProvider());
  }
}
