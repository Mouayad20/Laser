import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeal } from '../deal.model';
import { DealService } from '../service/deal.service';

@Component({
  templateUrl: './deal-delete-dialog.component.html',
})
export class DealDeleteDialogComponent {
  deal?: IDeal;

  constructor(protected dealService: DealService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dealService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
