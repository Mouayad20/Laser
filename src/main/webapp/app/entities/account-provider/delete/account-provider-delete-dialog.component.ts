import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAccountProvider } from '../account-provider.model';
import { AccountProviderService } from '../service/account-provider.service';

@Component({
  templateUrl: './account-provider-delete-dialog.component.html',
})
export class AccountProviderDeleteDialogComponent {
  accountProvider?: IAccountProvider;

  constructor(protected accountProviderService: AccountProviderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accountProviderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
