import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserApplicationComponent } from './list/user-application.component';
import { UserApplicationDetailComponent } from './detail/user-application-detail.component';
import { UserApplicationUpdateComponent } from './update/user-application-update.component';
import { UserApplicationDeleteDialogComponent } from './delete/user-application-delete-dialog.component';
import { UserApplicationRoutingModule } from './route/user-application-routing.module';

@NgModule({
  imports: [SharedModule, UserApplicationRoutingModule],
  declarations: [
    UserApplicationComponent,
    UserApplicationDetailComponent,
    UserApplicationUpdateComponent,
    UserApplicationDeleteDialogComponent,
  ],
  entryComponents: [UserApplicationDeleteDialogComponent],
})
export class UserApplicationModule {}
