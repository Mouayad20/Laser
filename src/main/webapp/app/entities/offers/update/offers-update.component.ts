import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOffers, Offers } from '../offers.model';
import { OffersService } from '../service/offers.service';

@Component({
  selector: 'jhi-offers-update',
  templateUrl: './offers-update.component.html',
})
export class OffersUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    shipmentDealId: [],
    tripDealId: [],
    status: [],
    senderId: [],
  });

  constructor(protected offersService: OffersService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ offers }) => {
      this.updateForm(offers);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const offers = this.createFromForm();
    if (offers.id !== undefined) {
      this.subscribeToSaveResponse(this.offersService.update(offers));
    } else {
      this.subscribeToSaveResponse(this.offersService.create(offers));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOffers>>): void {
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

  protected updateForm(offers: IOffers): void {
    this.editForm.patchValue({
      id: offers.id,
      shipmentDealId: offers.shipmentDealId,
      tripDealId: offers.tripDealId,
      status: offers.status,
      senderId: offers.senderId,
    });
  }

  protected createFromForm(): IOffers {
    return {
      ...new Offers(),
      id: this.editForm.get(['id'])!.value,
      shipmentDealId: this.editForm.get(['shipmentDealId'])!.value,
      tripDealId: this.editForm.get(['tripDealId'])!.value,
      status: this.editForm.get(['status'])!.value,
      senderId: this.editForm.get(['senderId'])!.value,
    };
  }
}
