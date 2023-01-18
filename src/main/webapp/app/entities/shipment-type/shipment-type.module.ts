import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ShipmentTypeComponent } from './list/shipment-type.component';
import { ShipmentTypeDetailComponent } from './detail/shipment-type-detail.component';
import { ShipmentTypeUpdateComponent } from './update/shipment-type-update.component';
import { ShipmentTypeDeleteDialogComponent } from './delete/shipment-type-delete-dialog.component';
import { ShipmentTypeRoutingModule } from './route/shipment-type-routing.module';

@NgModule({
  imports: [SharedModule, ShipmentTypeRoutingModule],
  declarations: [ShipmentTypeComponent, ShipmentTypeDetailComponent, ShipmentTypeUpdateComponent, ShipmentTypeDeleteDialogComponent],
  entryComponents: [ShipmentTypeDeleteDialogComponent],
})
export class ShipmentTypeModule {}
