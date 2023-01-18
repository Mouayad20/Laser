import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OffersComponent } from '../list/offers.component';
import { OffersDetailComponent } from '../detail/offers-detail.component';
import { OffersUpdateComponent } from '../update/offers-update.component';
import { OffersRoutingResolveService } from './offers-routing-resolve.service';

const offersRoute: Routes = [
  {
    path: '',
    component: OffersComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OffersDetailComponent,
    resolve: {
      offers: OffersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OffersUpdateComponent,
    resolve: {
      offers: OffersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OffersUpdateComponent,
    resolve: {
      offers: OffersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(offersRoute)],
  exports: [RouterModule],
})
export class OffersRoutingModule {}
