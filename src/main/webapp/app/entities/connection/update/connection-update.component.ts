import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IConnection, Connection } from '../connection.model';
import { ConnectionService } from '../service/connection.service';

@Component({
  selector: 'jhi-connection-update',
  templateUrl: './connection-update.component.html',
})
export class ConnectionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fcmToken: [],
    localToken: [],
    localRefreshToken: [],
    oAuthToken: [],
    localTokenExpiryDate: [],
  });

  constructor(protected connectionService: ConnectionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ connection }) => {
      if (connection.id === undefined) {
        const today = dayjs().startOf('day');
        connection.localTokenExpiryDate = today;
      }

      this.updateForm(connection);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const connection = this.createFromForm();
    if (connection.id !== undefined) {
      this.subscribeToSaveResponse(this.connectionService.update(connection));
    } else {
      this.subscribeToSaveResponse(this.connectionService.create(connection));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConnection>>): void {
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

  protected updateForm(connection: IConnection): void {
    this.editForm.patchValue({
      id: connection.id,
      fcmToken: connection.fcmToken,
      localToken: connection.localToken,
      localRefreshToken: connection.localRefreshToken,
      oAuthToken: connection.oAuthToken,
      localTokenExpiryDate: connection.localTokenExpiryDate ? connection.localTokenExpiryDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IConnection {
    return {
      ...new Connection(),
      id: this.editForm.get(['id'])!.value,
      fcmToken: this.editForm.get(['fcmToken'])!.value,
      localToken: this.editForm.get(['localToken'])!.value,
      localRefreshToken: this.editForm.get(['localRefreshToken'])!.value,
      oAuthToken: this.editForm.get(['oAuthToken'])!.value,
      localTokenExpiryDate: this.editForm.get(['localTokenExpiryDate'])!.value
        ? dayjs(this.editForm.get(['localTokenExpiryDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
