import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConnectionComponent } from '../list/connection.component';
import { ConnectionDetailComponent } from '../detail/connection-detail.component';
import { ConnectionUpdateComponent } from '../update/connection-update.component';
import { ConnectionRoutingResolveService } from './connection-routing-resolve.service';

const connectionRoute: Routes = [
  {
    path: '',
    component: ConnectionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConnectionDetailComponent,
    resolve: {
      connection: ConnectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConnectionUpdateComponent,
    resolve: {
      connection: ConnectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConnectionUpdateComponent,
    resolve: {
      connection: ConnectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(connectionRoute)],
  exports: [RouterModule],
})
export class ConnectionRoutingModule {}
