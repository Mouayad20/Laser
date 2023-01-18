import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITrip, Trip } from '../trip.model';
import { TripService } from '../service/trip.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';

@Component({
  selector: 'jhi-trip-update',
  templateUrl: './trip-update.component.html',
})
export class TripUpdateComponent implements OnInit {
  isSaving = false;

  locationsSharedCollection: ILocation[] = [];

  editForm = this.fb.group({
    id: [],
    createdAt: [],
    flyTime: [],
    arriveTime: [],
    tripIdentifier: [],
    details: [],
    ticketImage: [],
    tripType: [],
    transit: [],
    to: [],
    from: [],
  });

  constructor(
    protected tripService: TripService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trip }) => {
      if (trip.id === undefined) {
        const today = dayjs().startOf('day');
        trip.createdAt = today;
        trip.flyTime = today;
        trip.arriveTime = today;
      }

      this.updateForm(trip);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trip = this.createFromForm();
    if (trip.id !== undefined) {
      this.subscribeToSaveResponse(this.tripService.update(trip));
    } else {
      this.subscribeToSaveResponse(this.tripService.create(trip));
    }
  }

  trackLocationById(_index: number, item: ILocation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrip>>): void {
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

  protected updateForm(trip: ITrip): void {
    this.editForm.patchValue({
      id: trip.id,
      createdAt: trip.createdAt ? trip.createdAt.format(DATE_TIME_FORMAT) : null,
      flyTime: trip.flyTime ? trip.flyTime.format(DATE_TIME_FORMAT) : null,
      arriveTime: trip.arriveTime ? trip.arriveTime.format(DATE_TIME_FORMAT) : null,
      tripIdentifier: trip.tripIdentifier,
      details: trip.details,
      ticketImage: trip.ticketImage,
      tripType: trip.tripType,
      transit: trip.transit,
      to: trip.to,
      from: trip.from,
    });

    this.locationsSharedCollection = this.locationService.addLocationToCollectionIfMissing(
      this.locationsSharedCollection,
      trip.to,
      trip.from
    );
  }

  protected loadRelationshipsOptions(): void {
    this.locationService
      .query()
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing(locations, this.editForm.get('to')!.value, this.editForm.get('from')!.value)
        )
      )
      .subscribe((locations: ILocation[]) => (this.locationsSharedCollection = locations));
  }

  protected createFromForm(): ITrip {
    return {
      ...new Trip(),
      id: this.editForm.get(['id'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      flyTime: this.editForm.get(['flyTime'])!.value ? dayjs(this.editForm.get(['flyTime'])!.value, DATE_TIME_FORMAT) : undefined,
      arriveTime: this.editForm.get(['arriveTime'])!.value ? dayjs(this.editForm.get(['arriveTime'])!.value, DATE_TIME_FORMAT) : undefined,
      tripIdentifier: this.editForm.get(['tripIdentifier'])!.value,
      details: this.editForm.get(['details'])!.value,
      ticketImage: this.editForm.get(['ticketImage'])!.value,
      tripType: this.editForm.get(['tripType'])!.value,
      transit: this.editForm.get(['transit'])!.value,
      to: this.editForm.get(['to'])!.value,
      from: this.editForm.get(['from'])!.value,
    };
  }
}
