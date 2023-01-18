import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DealStatusComponent } from '../list/deal-status.component';
import { DealStatusDetailComponent } from '../detail/deal-status-detail.component';
import { DealStatusUpdateComponent } from '../update/deal-status-update.component';
import { DealStatusRoutingResolveService } from './deal-status-routing-resolve.service';

const dealStatusRoute: Routes = [
  {
    path: '',
    component: DealStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DealStatusDetailComponent,
    resolve: {
      dealStatus: DealStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DealStatusUpdateComponent,
    resolve: {
      dealStatus: DealStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DealStatusUpdateComponent,
    resolve: {
      dealStatus: DealStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dealStatusRoute)],
  exports: [RouterModule],
})
export class DealStatusRoutingModule {}
