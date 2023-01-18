import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OffersComponent } from './list/offers.component';
import { OffersDetailComponent } from './detail/offers-detail.component';
import { OffersUpdateComponent } from './update/offers-update.component';
import { OffersDeleteDialogComponent } from './delete/offers-delete-dialog.component';
import { OffersRoutingModule } from './route/offers-routing.module';

@NgModule({
  imports: [SharedModule, OffersRoutingModule],
  declarations: [OffersComponent, OffersDetailComponent, OffersUpdateComponent, OffersDeleteDialogComponent],
  entryComponents: [OffersDeleteDialogComponent],
})
export class OffersModule {}
