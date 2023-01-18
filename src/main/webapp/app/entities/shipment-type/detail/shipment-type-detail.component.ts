import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IShipmentType } from '../shipment-type.model';

@Component({
  selector: 'jhi-shipment-type-detail',
  templateUrl: './shipment-type-detail.component.html',
})
export class ShipmentTypeDetailComponent implements OnInit {
  shipmentType: IShipmentType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shipmentType }) => {
      this.shipmentType = shipmentType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
