import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AccountProviderComponent } from '../list/account-provider.component';
import { AccountProviderDetailComponent } from '../detail/account-provider-detail.component';
import { AccountProviderUpdateComponent } from '../update/account-provider-update.component';
import { AccountProviderRoutingResolveService } from './account-provider-routing-resolve.service';

const accountProviderRoute: Routes = [
  {
    path: '',
    component: AccountProviderComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountProviderDetailComponent,
    resolve: {
      accountProvider: AccountProviderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccountProviderUpdateComponent,
    resolve: {
      accountProvider: AccountProviderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccountProviderUpdateComponent,
    resolve: {
      accountProvider: AccountProviderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(accountProviderRoute)],
  exports: [RouterModule],
})
export class AccountProviderRoutingModule {}
