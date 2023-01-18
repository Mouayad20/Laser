import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DealStatusComponent } from './list/deal-status.component';
import { DealStatusDetailComponent } from './detail/deal-status-detail.component';
import { DealStatusUpdateComponent } from './update/deal-status-update.component';
import { DealStatusDeleteDialogComponent } from './delete/deal-status-delete-dialog.component';
import { DealStatusRoutingModule } from './route/deal-status-routing.module';

@NgModule({
  imports: [SharedModule, DealStatusRoutingModule],
  declarations: [DealStatusComponent, DealStatusDetailComponent, DealStatusUpdateComponent, DealStatusDeleteDialogComponent],
  entryComponents: [DealStatusDeleteDialogComponent],
})
export class DealStatusModule {}
