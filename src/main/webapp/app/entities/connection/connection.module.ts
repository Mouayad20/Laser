import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConnectionComponent } from './list/connection.component';
import { ConnectionDetailComponent } from './detail/connection-detail.component';
import { ConnectionUpdateComponent } from './update/connection-update.component';
import { ConnectionDeleteDialogComponent } from './delete/connection-delete-dialog.component';
import { ConnectionRoutingModule } from './route/connection-routing.module';

@NgModule({
  imports: [SharedModule, ConnectionRoutingModule],
  declarations: [ConnectionComponent, ConnectionDetailComponent, ConnectionUpdateComponent, ConnectionDeleteDialogComponent],
  entryComponents: [ConnectionDeleteDialogComponent],
})
export class ConnectionModule {}
