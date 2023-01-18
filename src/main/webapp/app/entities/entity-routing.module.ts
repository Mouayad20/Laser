import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'article',
        data: { pageTitle: 'laserApp.article.home.title' },
        loadChildren: () => import('./article/article.module').then(m => m.ArticleModule),
      },
      {
        path: 'user-application',
        data: { pageTitle: 'laserApp.userApplication.home.title' },
        loadChildren: () => import('./user-application/user-application.module').then(m => m.UserApplicationModule),
      },
      {
        path: 'deal',
        data: { pageTitle: 'laserApp.deal.home.title' },
        loadChildren: () => import('./deal/deal.module').then(m => m.DealModule),
      },
      {
        path: 'transaction',
        data: { pageTitle: 'laserApp.transaction.home.title' },
        loadChildren: () => import('./transaction/transaction.module').then(m => m.TransactionModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'laserApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'shipment',
        data: { pageTitle: 'laserApp.shipment.home.title' },
        loadChildren: () => import('./shipment/shipment.module').then(m => m.ShipmentModule),
      },
      {
        path: 'connection',
        data: { pageTitle: 'laserApp.connection.home.title' },
        loadChildren: () => import('./connection/connection.module').then(m => m.ConnectionModule),
      },
      {
        path: 'trip',
        data: { pageTitle: 'laserApp.trip.home.title' },
        loadChildren: () => import('./trip/trip.module').then(m => m.TripModule),
      },
      {
        path: 'account-provider',
        data: { pageTitle: 'laserApp.accountProvider.home.title' },
        loadChildren: () => import('./account-provider/account-provider.module').then(m => m.AccountProviderModule),
      },
      {
        path: 'shipment-type',
        data: { pageTitle: 'laserApp.shipmentType.home.title' },
        loadChildren: () => import('./shipment-type/shipment-type.module').then(m => m.ShipmentTypeModule),
      },
      {
        path: 'deal-status',
        data: { pageTitle: 'laserApp.dealStatus.home.title' },
        loadChildren: () => import('./deal-status/deal-status.module').then(m => m.DealStatusModule),
      },
      {
        path: 'offers',
        data: { pageTitle: 'laserApp.offers.home.title' },
        loadChildren: () => import('./offers/offers.module').then(m => m.OffersModule),
      },
      {
        path: 'constants',
        data: { pageTitle: 'laserApp.constants.home.title' },
        loadChildren: () => import('./constants/constants.module').then(m => m.ConstantsModule),
      },
      {
        path: 'countries',
        data: { pageTitle: 'laserApp.countries.home.title' },
        loadChildren: () => import('./countries/countries.module').then(m => m.CountriesModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
