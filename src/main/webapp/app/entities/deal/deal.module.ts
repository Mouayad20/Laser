import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DealComponent } from './list/deal.component';
import { DealDetailComponent } from './detail/deal-detail.component';
import { DealUpdateComponent } from './update/deal-update.component';
import { DealDeleteDialogComponent } from './delete/deal-delete-dialog.component';
import { DealRoutingModule } from './route/deal-routing.module';

@NgModule({
  imports: [SharedModule, DealRoutingModule],
  declarations: [DealComponent, DealDetailComponent, DealUpdateComponent, DealDeleteDialogComponent],
  entryComponents: [DealDeleteDialogComponent],
})
export class DealModule {}
