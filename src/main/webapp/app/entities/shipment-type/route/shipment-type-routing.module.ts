import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ShipmentTypeComponent } from '../list/shipment-type.component';
import { ShipmentTypeDetailComponent } from '../detail/shipment-type-detail.component';
import { ShipmentTypeUpdateComponent } from '../update/shipment-type-update.component';
import { ShipmentTypeRoutingResolveService } from './shipment-type-routing-resolve.service';

const shipmentTypeRoute: Routes = [
  {
    path: '',
    component: ShipmentTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ShipmentTypeDetailComponent,
    resolve: {
      shipmentType: ShipmentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ShipmentTypeUpdateComponent,
    resolve: {
      shipmentType: ShipmentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ShipmentTypeUpdateComponent,
    resolve: {
      shipmentType: ShipmentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(shipmentTypeRoute)],
  exports: [RouterModule],
})
export class ShipmentTypeRoutingModule {}
