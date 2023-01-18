import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConnection, Connection } from '../connection.model';
import { ConnectionService } from '../service/connection.service';

@Injectable({ providedIn: 'root' })
export class ConnectionRoutingResolveService implements Resolve<IConnection> {
  constructor(protected service: ConnectionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConnection> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((connection: HttpResponse<Connection>) => {
          if (connection.body) {
            return of(connection.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Connection());
  }
}
