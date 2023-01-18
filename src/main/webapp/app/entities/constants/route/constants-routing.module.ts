import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConstantsComponent } from '../list/constants.component';
import { ConstantsDetailComponent } from '../detail/constants-detail.component';
import { ConstantsUpdateComponent } from '../update/constants-update.component';
import { ConstantsRoutingResolveService } from './constants-routing-resolve.service';

const constantsRoute: Routes = [
  {
    path: '',
    component: ConstantsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConstantsDetailComponent,
    resolve: {
      constants: ConstantsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConstantsUpdateComponent,
    resolve: {
      constants: ConstantsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConstantsUpdateComponent,
    resolve: {
      constants: ConstantsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(constantsRoute)],
  exports: [RouterModule],
})
export class ConstantsRoutingModule {}
