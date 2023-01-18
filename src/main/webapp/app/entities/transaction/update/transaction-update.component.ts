import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITransaction, Transaction } from '../transaction.model';
import { TransactionService } from '../service/transaction.service';
import { IAccountProvider } from 'app/entities/account-provider/account-provider.model';
import { AccountProviderService } from 'app/entities/account-provider/service/account-provider.service';

@Component({
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html',
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;

  accountProvidersSharedCollection: IAccountProvider[] = [];

  editForm = this.fb.group({
    id: [],
    fromAccount: [],
    toAccount: [],
    fees: [],
    netAmount: [],
    details: [],
    provider: [],
  });

  constructor(
    protected transactionService: TransactionService,
    protected accountProviderService: AccountProviderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.updateForm(transaction);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaction = this.createFromForm();
    if (transaction.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  trackAccountProviderById(_index: number, item: IAccountProvider): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
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

  protected updateForm(transaction: ITransaction): void {
    this.editForm.patchValue({
      id: transaction.id,
      fromAccount: transaction.fromAccount,
      toAccount: transaction.toAccount,
      fees: transaction.fees,
      netAmount: transaction.netAmount,
      details: transaction.details,
      provider: transaction.provider,
    });

    this.accountProvidersSharedCollection = this.accountProviderService.addAccountProviderToCollectionIfMissing(
      this.accountProvidersSharedCollection,
      transaction.provider
    );
  }

  protected loadRelationshipsOptions(): void {
    this.accountProviderService
      .query()
      .pipe(map((res: HttpResponse<IAccountProvider[]>) => res.body ?? []))
      .pipe(
        map((accountProviders: IAccountProvider[]) =>
          this.accountProviderService.addAccountProviderToCollectionIfMissing(accountProviders, this.editForm.get('provider')!.value)
        )
      )
      .subscribe((accountProviders: IAccountProvider[]) => (this.accountProvidersSharedCollection = accountProviders));
  }

  protected createFromForm(): ITransaction {
    return {
      ...new Transaction(),
      id: this.editForm.get(['id'])!.value,
      fromAccount: this.editForm.get(['fromAccount'])!.value,
      toAccount: this.editForm.get(['toAccount'])!.value,
      fees: this.editForm.get(['fees'])!.value,
      netAmount: this.editForm.get(['netAmount'])!.value,
      details: this.editForm.get(['details'])!.value,
      provider: this.editForm.get(['provider'])!.value,
    };
  }
}
