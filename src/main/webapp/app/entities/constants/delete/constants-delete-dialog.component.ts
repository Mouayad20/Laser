import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConstants } from '../constants.model';
import { ConstantsService } from '../service/constants.service';

@Component({
  templateUrl: './constants-delete-dialog.component.html',
})
export class ConstantsDeleteDialogComponent {
  constants?: IConstants;

  constructor(protected constantsService: ConstantsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.constantsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
