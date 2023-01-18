import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CountriesComponent } from './list/countries.component';
import { CountriesDetailComponent } from './detail/countries-detail.component';
import { CountriesUpdateComponent } from './update/countries-update.component';
import { CountriesDeleteDialogComponent } from './delete/countries-delete-dialog.component';
import { CountriesRoutingModule } from './route/countries-routing.module';

@NgModule({
  imports: [SharedModule, CountriesRoutingModule],
  declarations: [CountriesComponent, CountriesDetailComponent, CountriesUpdateComponent, CountriesDeleteDialogComponent],
  entryComponents: [CountriesDeleteDialogComponent],
})
export class CountriesModule {}
