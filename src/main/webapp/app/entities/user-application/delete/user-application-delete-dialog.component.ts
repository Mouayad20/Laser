import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserApplication } from '../user-application.model';
import { UserApplicationService } from '../service/user-application.service';

@Component({
  templateUrl: './user-application-delete-dialog.component.html',
})
export class UserApplicationDeleteDialogComponent {
  userApplication?: IUserApplication;

  constructor(protected userApplicationService: UserApplicationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userApplicationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
