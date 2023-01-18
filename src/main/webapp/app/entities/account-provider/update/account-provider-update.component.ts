import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAccountProvider, AccountProvider } from '../account-provider.model';
import { AccountProviderService } from '../service/account-provider.service';

@Component({
  selector: 'jhi-account-provider-update',
  templateUrl: './account-provider-update.component.html',
})
export class AccountProviderUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(
    protected accountProviderService: AccountProviderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountProvider }) => {
      this.updateForm(accountProvider);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accountProvider = this.createFromForm();
    if (accountProvider.id !== undefined) {
      this.subscribeToSaveResponse(this.accountProviderService.update(accountProvider));
    } else {
      this.subscribeToSaveResponse(this.accountProviderService.create(accountProvider));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountProvider>>): void {
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

  protected updateForm(accountProvider: IAccountProvider): void {
    this.editForm.patchValue({
      id: accountProvider.id,
      name: accountProvider.name,
    });
  }

  protected createFromForm(): IAccountProvider {
    return {
      ...new AccountProvider(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
