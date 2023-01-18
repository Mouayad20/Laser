import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrip } from '../trip.model';
import { TripService } from '../service/trip.service';

@Component({
  templateUrl: './trip-delete-dialog.component.html',
})
export class TripDeleteDialogComponent {
  trip?: ITrip;

  constructor(protected tripService: TripService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tripService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
