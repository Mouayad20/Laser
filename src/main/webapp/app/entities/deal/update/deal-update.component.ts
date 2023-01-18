import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDeal, Deal } from '../deal.model';
import { DealService } from '../service/deal.service';
import { ITransaction } from 'app/entities/transaction/transaction.model';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';
import { IUserApplication } from 'app/entities/user-application/user-application.model';
import { UserApplicationService } from 'app/entities/user-application/service/user-application.service';
import { ITrip } from 'app/entities/trip/trip.model';
import { TripService } from 'app/entities/trip/service/trip.service';
import { IDealStatus } from 'app/entities/deal-status/deal-status.model';
import { DealStatusService } from 'app/entities/deal-status/service/deal-status.service';

@Component({
  selector: 'jhi-deal-update',
  templateUrl: './deal-update.component.html',
})
export class DealUpdateComponent implements OnInit {
  isSaving = false;

  transactionsCollection: ITransaction[] = [];
  userApplicationsSharedCollection: IUserApplication[] = [];
  tripsSharedCollection: ITrip[] = [];
  dealStatusesSharedCollection: IDealStatus[] = [];

  editForm = this.fb.group({
    id: [],
    totalPrice: [],
    isCashed: [],
    fromAccount: [],
    toAccount: [],
    fullWeight: [],
    availableWeight: [],
    arrivelDate: [],
    expectedDate: [],
    details: [],
    transaction: [],
    deliver: [],
    owner: [],
    trip: [],
    status: [],
  });

  constructor(
    protected dealService: DealService,
    protected transactionService: TransactionService,
    protected userApplicationService: UserApplicationService,
    protected tripService: TripService,
    protected dealStatusService: DealStatusService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deal }) => {
      if (deal.id === undefined) {
        const today = dayjs().startOf('day');
        deal.arrivelDate = today;
        deal.expectedDate = today;
      }

      this.updateForm(deal);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deal = this.createFromForm();
    if (deal.id !== undefined) {
      this.subscribeToSaveResponse(this.dealService.update(deal));
    } else {
      this.subscribeToSaveResponse(this.dealService.create(deal));
    }
  }

  trackTransactionById(_index: number, item: ITransaction): number {
    return item.id!;
  }

  trackUserApplicationById(_index: number, item: IUserApplication): number {
    return item.id!;
  }

  trackTripById(_index: number, item: ITrip): number {
    return item.id!;
  }

  trackDealStatusById(_index: number, item: IDealStatus): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeal>>): void {
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

  protected updateForm(deal: IDeal): void {
    this.editForm.patchValue({
      id: deal.id,
      totalPrice: deal.totalPrice,
      isCashed: deal.isCashed,
      fromAccount: deal.fromAccount,
      toAccount: deal.toAccount,
      fullWeight: deal.fullWeight,
      availableWeight: deal.availableWeight,
      arrivelDate: deal.arrivelDate ? deal.arrivelDate.format(DATE_TIME_FORMAT) : null,
      expectedDate: deal.expectedDate ? deal.expectedDate.format(DATE_TIME_FORMAT) : null,
      details: deal.details,
      transaction: deal.transaction,
      deliver: deal.deliver,
      owner: deal.owner,
      trip: deal.trip,
      status: deal.status,
    });

    this.transactionsCollection = this.transactionService.addTransactionToCollectionIfMissing(
      this.transactionsCollection,
      deal.transaction
    );
    this.userApplicationsSharedCollection = this.userApplicationService.addUserApplicationToCollectionIfMissing(
      this.userApplicationsSharedCollection,
      deal.deliver,
      deal.owner
    );
    this.tripsSharedCollection = this.tripService.addTripToCollectionIfMissing(this.tripsSharedCollection, deal.trip);
    this.dealStatusesSharedCollection = this.dealStatusService.addDealStatusToCollectionIfMissing(
      this.dealStatusesSharedCollection,
      deal.status
    );
  }

  protected loadRelationshipsOptions(): void {
    this.transactionService
      .query({ filter: 'deal-is-null' })
      .pipe(map((res: HttpResponse<ITransaction[]>) => res.body ?? []))
      .pipe(
        map((transactions: ITransaction[]) =>
          this.transactionService.addTransactionToCollectionIfMissing(transactions, this.editForm.get('transaction')!.value)
        )
      )
      .subscribe((transactions: ITransaction[]) => (this.transactionsCollection = transactions));

    this.userApplicationService
      .query()
      .pipe(map((res: HttpResponse<IUserApplication[]>) => res.body ?? []))
      .pipe(
        map((userApplications: IUserApplication[]) =>
          this.userApplicationService.addUserApplicationToCollectionIfMissing(
            userApplications,
            this.editForm.get('deliver')!.value,
            this.editForm.get('owner')!.value
          )
        )
      )
      .subscribe((userApplications: IUserApplication[]) => (this.userApplicationsSharedCollection = userApplications));

    this.tripService
      .query()
      .pipe(map((res: HttpResponse<ITrip[]>) => res.body ?? []))
      .pipe(map((trips: ITrip[]) => this.tripService.addTripToCollectionIfMissing(trips, this.editForm.get('trip')!.value)))
      .subscribe((trips: ITrip[]) => (this.tripsSharedCollection = trips));

    this.dealStatusService
      .query()
      .pipe(map((res: HttpResponse<IDealStatus[]>) => res.body ?? []))
      .pipe(
        map((dealStatuses: IDealStatus[]) =>
          this.dealStatusService.addDealStatusToCollectionIfMissing(dealStatuses, this.editForm.get('status')!.value)
        )
      )
      .subscribe((dealStatuses: IDealStatus[]) => (this.dealStatusesSharedCollection = dealStatuses));
  }

  protected createFromForm(): IDeal {
    return {
      ...new Deal(),
      id: this.editForm.get(['id'])!.value,
      totalPrice: this.editForm.get(['totalPrice'])!.value,
      isCashed: this.editForm.get(['isCashed'])!.value,
      fromAccount: this.editForm.get(['fromAccount'])!.value,
      toAccount: this.editForm.get(['toAccount'])!.value,
      fullWeight: this.editForm.get(['fullWeight'])!.value,
      availableWeight: this.editForm.get(['availableWeight'])!.value,
      arrivelDate: this.editForm.get(['arrivelDate'])!.value
        ? dayjs(this.editForm.get(['arrivelDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      expectedDate: this.editForm.get(['expectedDate'])!.value
        ? dayjs(this.editForm.get(['expectedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      details: this.editForm.get(['details'])!.value,
      transaction: this.editForm.get(['transaction'])!.value,
      deliver: this.editForm.get(['deliver'])!.value,
      owner: this.editForm.get(['owner'])!.value,
      trip: this.editForm.get(['trip'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }
}
