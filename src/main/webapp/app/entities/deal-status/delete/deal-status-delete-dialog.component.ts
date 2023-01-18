import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDealStatus } from '../deal-status.model';
import { DealStatusService } from '../service/deal-status.service';

@Component({
  templateUrl: './deal-status-delete-dialog.component.html',
})
export class DealStatusDeleteDialogComponent {
  dealStatus?: IDealStatus;

  constructor(protected dealStatusService: DealStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dealStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
