import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConnection } from '../connection.model';
import { ConnectionService } from '../service/connection.service';

@Component({
  templateUrl: './connection-delete-dialog.component.html',
})
export class ConnectionDeleteDialogComponent {
  connection?: IConnection;

  constructor(protected connectionService: ConnectionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.connectionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
