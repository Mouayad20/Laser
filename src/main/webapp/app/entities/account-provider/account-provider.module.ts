import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AccountProviderComponent } from './list/account-provider.component';
import { AccountProviderDetailComponent } from './detail/account-provider-detail.component';
import { AccountProviderUpdateComponent } from './update/account-provider-update.component';
import { AccountProviderDeleteDialogComponent } from './delete/account-provider-delete-dialog.component';
import { AccountProviderRoutingModule } from './route/account-provider-routing.module';

@NgModule({
  imports: [SharedModule, AccountProviderRoutingModule],
  declarations: [
    AccountProviderComponent,
    AccountProviderDetailComponent,
    AccountProviderUpdateComponent,
    AccountProviderDeleteDialogComponent,
  ],
  entryComponents: [AccountProviderDeleteDialogComponent],
})
export class AccountProviderModule {}
