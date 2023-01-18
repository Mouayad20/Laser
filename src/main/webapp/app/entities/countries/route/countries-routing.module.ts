import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CountriesComponent } from '../list/countries.component';
import { CountriesDetailComponent } from '../detail/countries-detail.component';
import { CountriesUpdateComponent } from '../update/countries-update.component';
import { CountriesRoutingResolveService } from './countries-routing-resolve.service';

const countriesRoute: Routes = [
  {
    path: '',
    component: CountriesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CountriesDetailComponent,
    resolve: {
      countries: CountriesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CountriesUpdateComponent,
    resolve: {
      countries: CountriesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CountriesUpdateComponent,
    resolve: {
      countries: CountriesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(countriesRoute)],
  exports: [RouterModule],
})
export class CountriesRoutingModule {}
