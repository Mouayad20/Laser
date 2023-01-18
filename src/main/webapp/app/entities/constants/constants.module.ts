import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConstantsComponent } from './list/constants.component';
import { ConstantsDetailComponent } from './detail/constants-detail.component';
import { ConstantsUpdateComponent } from './update/constants-update.component';
import { ConstantsDeleteDialogComponent } from './delete/constants-delete-dialog.component';
import { ConstantsRoutingModule } from './route/constants-routing.module';

@NgModule({
  imports: [SharedModule, ConstantsRoutingModule],
  declarations: [ConstantsComponent, ConstantsDetailComponent, ConstantsUpdateComponent, ConstantsDeleteDialogComponent],
  entryComponents: [ConstantsDeleteDialogComponent],
})
export class ConstantsModule {}
