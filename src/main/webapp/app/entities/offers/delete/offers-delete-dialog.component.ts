import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOffers } from '../offers.model';
import { OffersService } from '../service/offers.service';

@Component({
  templateUrl: './offers-delete-dialog.component.html',
})
export class OffersDeleteDialogComponent {
  offers?: IOffers;

  constructor(protected offersService: OffersService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.offersService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
