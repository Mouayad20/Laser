import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDealStatus, DealStatus } from '../deal-status.model';
import { DealStatusService } from '../service/deal-status.service';

@Component({
  selector: 'jhi-deal-status-update',
  templateUrl: './deal-status-update.component.html',
})
export class DealStatusUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    sequence: [],
  });

  constructor(protected dealStatusService: DealStatusService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dealStatus }) => {
      this.updateForm(dealStatus);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dealStatus = this.createFromForm();
    if (dealStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.dealStatusService.update(dealStatus));
    } else {
      this.subscribeToSaveResponse(this.dealStatusService.create(dealStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDealStatus>>): void {
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

  protected updateForm(dealStatus: IDealStatus): void {
    this.editForm.patchValue({
      id: dealStatus.id,
      name: dealStatus.name,
      sequence: dealStatus.sequence,
    });
  }

  protected createFromForm(): IDealStatus {
    return {
      ...new DealStatus(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      sequence: this.editForm.get(['sequence'])!.value,
    };
  }
}
