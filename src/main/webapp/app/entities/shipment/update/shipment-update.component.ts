import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IShipment, Shipment } from '../shipment.model';
import { ShipmentService } from '../service/shipment.service';
import { IShipmentType } from 'app/entities/shipment-type/shipment-type.model';
import { ShipmentTypeService } from 'app/entities/shipment-type/service/shipment-type.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { IDeal } from 'app/entities/deal/deal.model';
import { DealService } from 'app/entities/deal/service/deal.service';

@Component({
  selector: 'jhi-shipment-update',
  templateUrl: './shipment-update.component.html',
})
export class ShipmentUpdateComponent implements OnInit {
  isSaving = false;

  shipmentTypesSharedCollection: IShipmentType[] = [];
  locationsSharedCollection: ILocation[] = [];
  dealsSharedCollection: IDeal[] = [];

  editForm = this.fb.group({
    id: [],
    weight: [],
    url: [],
    description: [],
    createdAt: [],
    imgUrl: [],
    cost: [],
    price: [],
    details: [],
    type: [],
    to: [],
    from: [],
    deal: [],
  });

  constructor(
    protected shipmentService: ShipmentService,
    protected shipmentTypeService: ShipmentTypeService,
    protected locationService: LocationService,
    protected dealService: DealService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shipment }) => {
      if (shipment.id === undefined) {
        const today = dayjs().startOf('day');
        shipment.createdAt = today;
      }

      this.updateForm(shipment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shipment = this.createFromForm();
    if (shipment.id !== undefined) {
      this.subscribeToSaveResponse(this.shipmentService.update(shipment));
    } else {
      this.subscribeToSaveResponse(this.shipmentService.create(shipment));
    }
  }

  trackShipmentTypeById(_index: number, item: IShipmentType): number {
    return item.id!;
  }

  trackLocationById(_index: number, item: ILocation): number {
    return item.id!;
  }

  trackDealById(_index: number, item: IDeal): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShipment>>): void {
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

  protected updateForm(shipment: IShipment): void {
    this.editForm.patchValue({
      id: shipment.id,
      weight: shipment.weight,
      url: shipment.url,
      description: shipment.description,
      createdAt: shipment.createdAt ? shipment.createdAt.format(DATE_TIME_FORMAT) : null,
      imgUrl: shipment.imgUrl,
      cost: shipment.cost,
      price: shipment.price,
      details: shipment.details,
      type: shipment.type,
      to: shipment.to,
      from: shipment.from,
      deal: shipment.deal,
    });

    this.shipmentTypesSharedCollection = this.shipmentTypeService.addShipmentTypeToCollectionIfMissing(
      this.shipmentTypesSharedCollection,
      shipment.type
    );
    this.locationsSharedCollection = this.locationService.addLocationToCollectionIfMissing(
      this.locationsSharedCollection,
      shipment.to,
      shipment.from
    );
    this.dealsSharedCollection = this.dealService.addDealToCollectionIfMissing(this.dealsSharedCollection, shipment.deal);
  }

  protected loadRelationshipsOptions(): void {
    this.shipmentTypeService
      .query()
      .pipe(map((res: HttpResponse<IShipmentType[]>) => res.body ?? []))
      .pipe(
        map((shipmentTypes: IShipmentType[]) =>
          this.shipmentTypeService.addShipmentTypeToCollectionIfMissing(shipmentTypes, this.editForm.get('type')!.value)
        )
      )
      .subscribe((shipmentTypes: IShipmentType[]) => (this.shipmentTypesSharedCollection = shipmentTypes));

    this.locationService
      .query()
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing(locations, this.editForm.get('to')!.value, this.editForm.get('from')!.value)
        )
      )
      .subscribe((locations: ILocation[]) => (this.locationsSharedCollection = locations));

    this.dealService
      .query()
      .pipe(map((res: HttpResponse<IDeal[]>) => res.body ?? []))
      .pipe(map((deals: IDeal[]) => this.dealService.addDealToCollectionIfMissing(deals, this.editForm.get('deal')!.value)))
      .subscribe((deals: IDeal[]) => (this.dealsSharedCollection = deals));
  }

  protected createFromForm(): IShipment {
    return {
      ...new Shipment(),
      id: this.editForm.get(['id'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      url: this.editForm.get(['url'])!.value,
      description: this.editForm.get(['description'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      imgUrl: this.editForm.get(['imgUrl'])!.value,
      cost: this.editForm.get(['cost'])!.value,
      price: this.editForm.get(['price'])!.value,
      details: this.editForm.get(['details'])!.value,
      type: this.editForm.get(['type'])!.value,
      to: this.editForm.get(['to'])!.value,
      from: this.editForm.get(['from'])!.value,
      deal: this.editForm.get(['deal'])!.value,
    };
  }
}
