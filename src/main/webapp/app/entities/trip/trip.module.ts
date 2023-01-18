import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TripComponent } from './list/trip.component';
import { TripDetailComponent } from './detail/trip-detail.component';
import { TripUpdateComponent } from './update/trip-update.component';
import { TripDeleteDialogComponent } from './delete/trip-delete-dialog.component';
import { TripRoutingModule } from './route/trip-routing.module';

@NgModule({
  imports: [SharedModule, TripRoutingModule],
  declarations: [TripComponent, TripDetailComponent, TripUpdateComponent, TripDeleteDialogComponent],
  entryComponents: [TripDeleteDialogComponent],
})
export class TripModule {}
