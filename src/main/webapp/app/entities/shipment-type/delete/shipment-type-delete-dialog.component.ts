import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IShipmentType } from '../shipment-type.model';
import { ShipmentTypeService } from '../service/shipment-type.service';

@Component({
  templateUrl: './shipment-type-delete-dialog.component.html',
})
export class ShipmentTypeDeleteDialogComponent {
  shipmentType?: IShipmentType;

  constructor(protected shipmentTypeService: ShipmentTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.shipmentTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
