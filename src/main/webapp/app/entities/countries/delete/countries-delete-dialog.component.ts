import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICountries } from '../countries.model';
import { CountriesService } from '../service/countries.service';

@Component({
  templateUrl: './countries-delete-dialog.component.html',
})
export class CountriesDeleteDialogComponent {
  countries?: ICountries;

  constructor(protected countriesService: CountriesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.countriesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
