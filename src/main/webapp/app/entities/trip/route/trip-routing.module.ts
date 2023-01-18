import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TripComponent } from '../list/trip.component';
import { TripDetailComponent } from '../detail/trip-detail.component';
import { TripUpdateComponent } from '../update/trip-update.component';
import { TripRoutingResolveService } from './trip-routing-resolve.service';

const tripRoute: Routes = [
  {
    path: '',
    component: TripComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TripDetailComponent,
    resolve: {
      trip: TripRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TripUpdateComponent,
    resolve: {
      trip: TripRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TripUpdateComponent,
    resolve: {
      trip: TripRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tripRoute)],
  exports: [RouterModule],
})
export class TripRoutingModule {}
