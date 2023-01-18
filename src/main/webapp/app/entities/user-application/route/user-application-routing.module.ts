import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserApplicationComponent } from '../list/user-application.component';
import { UserApplicationDetailComponent } from '../detail/user-application-detail.component';
import { UserApplicationUpdateComponent } from '../update/user-application-update.component';
import { UserApplicationRoutingResolveService } from './user-application-routing-resolve.service';

const userApplicationRoute: Routes = [
  {
    path: '',
    component: UserApplicationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserApplicationDetailComponent,
    resolve: {
      userApplication: UserApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserApplicationUpdateComponent,
    resolve: {
      userApplication: UserApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserApplicationUpdateComponent,
    resolve: {
      userApplication: UserApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userApplicationRoute)],
  exports: [RouterModule],
})
export class UserApplicationRoutingModule {}
