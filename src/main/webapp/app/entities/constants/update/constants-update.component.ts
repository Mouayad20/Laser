import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IConstants, Constants } from '../constants.model';
import { ConstantsService } from '../service/constants.service';

@Component({
  selector: 'jhi-constants-update',
  templateUrl: './constants-update.component.html',
})
export class ConstantsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    weightFactor: [],
    maxWeight: [],
  });

  constructor(protected constantsService: ConstantsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ constants }) => {
      this.updateForm(constants);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const constants = this.createFromForm();
    if (constants.id !== undefined) {
      this.subscribeToSaveResponse(this.constantsService.update(constants));
    } else {
      this.subscribeToSaveResponse(this.constantsService.create(constants));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConstants>>): void {
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

  protected updateForm(constants: IConstants): void {
    this.editForm.patchValue({
      id: constants.id,
      weightFactor: constants.weightFactor,
      maxWeight: constants.maxWeight,
    });
  }

  protected createFromForm(): IConstants {
    return {
      ...new Constants(),
      id: this.editForm.get(['id'])!.value,
      weightFactor: this.editForm.get(['weightFactor'])!.value,
      maxWeight: this.editForm.get(['maxWeight'])!.value,
    };
  }
}
