import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DealComponent } from '../list/deal.component';
import { DealDetailComponent } from '../detail/deal-detail.component';
import { DealUpdateComponent } from '../update/deal-update.component';
import { DealRoutingResolveService } from './deal-routing-resolve.service';

const dealRoute: Routes = [
  {
    path: '',
    component: DealComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DealDetailComponent,
    resolve: {
      deal: DealRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DealUpdateComponent,
    resolve: {
      deal: DealRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DealUpdateComponent,
    resolve: {
      deal: DealRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dealRoute)],
  exports: [RouterModule],
})
export class DealRoutingModule {}
