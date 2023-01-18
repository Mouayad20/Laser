import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IShipmentType, ShipmentType } from '../shipment-type.model';
import { ShipmentTypeService } from '../service/shipment-type.service';

@Component({
  selector: 'jhi-shipment-type-update',
  templateUrl: './shipment-type-update.component.html',
})
export class ShipmentTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    factor: [],
  });

  constructor(protected shipmentTypeService: ShipmentTypeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shipmentType }) => {
      this.updateForm(shipmentType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shipmentType = this.createFromForm();
    if (shipmentType.id !== undefined) {
      this.subscribeToSaveResponse(this.shipmentTypeService.update(shipmentType));
    } else {
      this.subscribeToSaveResponse(this.shipmentTypeService.create(shipmentType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShipmentType>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(shipmentType: IShipmentType): void {
    this.editForm.patchValue({
      id: shipmentType.id,
      name: shipmentType.name,
      factor: shipmentType.factor,
    });
  }

  protected createFromForm(): IShipmentType {
    return {
      ...new ShipmentType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      factor: this.editForm.get(['factor'])!.value,
    };
  }
}
