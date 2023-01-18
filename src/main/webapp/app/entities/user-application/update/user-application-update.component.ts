import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IUserApplication, UserApplication } from '../user-application.model';
import { UserApplicationService } from '../service/user-application.service';
import { IConnection } from 'app/entities/connection/connection.model';
import { ConnectionService } from 'app/entities/connection/service/connection.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-user-application-update',
  templateUrl: './user-application-update.component.html',
})
export class UserApplicationUpdateComponent implements OnInit {
  isSaving = false;

  connectionsCollection: IConnection[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    phone: [],
    passport: [],
    createdAt: [],
    isGoogleAccount: [],
    isFacebookAccount: [],
    isTwitterAccount: [],
    image: [],
    rate: [],
    fiveStar: [],
    fourSatr: [],
    threeStar: [],
    twoStar: [],
    oneStar: [],
    detalis: [],
    connection: [],
    user: [],
  });

  constructor(
    protected userApplicationService: UserApplicationService,
    protected connectionService: ConnectionService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userApplication }) => {
      if (userApplication.id === undefined) {
        const today = dayjs().startOf('day');
        userApplication.createdAt = today;
      }

      this.updateForm(userApplication);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userApplication = this.createFromForm();
    if (userApplication.id !== undefined) {
      this.subscribeToSaveResponse(this.userApplicationService.update(userApplication));
    } else {
      this.subscribeToSaveResponse(this.userApplicationService.create(userApplication));
    }
  }

  trackConnectionById(_index: number, item: IConnection): number {
    return item.id!;
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserApplication>>): void {
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

  protected updateForm(userApplication: IUserApplication): void {
    this.editForm.patchValue({
      id: userApplication.id,
      phone: userApplication.phone,
      passport: userApplication.passport,
      createdAt: userApplication.createdAt ? userApplication.createdAt.format(DATE_TIME_FORMAT) : null,
      isGoogleAccount: userApplication.isGoogleAccount,
      isFacebookAccount: userApplication.isFacebookAccount,
      isTwitterAccount: userApplication.isTwitterAccount,
      image: userApplication.image,
      rate: userApplication.rate,
      fiveStar: userApplication.fiveStar,
      fourSatr: userApplication.fourSatr,
      threeStar: userApplication.threeStar,
      twoStar: userApplication.twoStar,
      oneStar: userApplication.oneStar,
      detalis: userApplication.detalis,
      connection: userApplication.connection,
      user: userApplication.user,
    });

    this.connectionsCollection = this.connectionService.addConnectionToCollectionIfMissing(
      this.connectionsCollection,
      userApplication.connection
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, userApplication.user);
  }

  protected loadRelationshipsOptions(): void {
    this.connectionService
      .query({ filter: 'userapplication-is-null' })
      .pipe(map((res: HttpResponse<IConnection[]>) => res.body ?? []))
      .pipe(
        map((connections: IConnection[]) =>
          this.connectionService.addConnectionToCollectionIfMissing(connections, this.editForm.get('connection')!.value)
        )
      )
      .subscribe((connections: IConnection[]) => (this.connectionsCollection = connections));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IUserApplication {
    return {
      ...new UserApplication(),
      id: this.editForm.get(['id'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      passport: this.editForm.get(['passport'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      isGoogleAccount: this.editForm.get(['isGoogleAccount'])!.value,
      isFacebookAccount: this.editForm.get(['isFacebookAccount'])!.value,
      isTwitterAccount: this.editForm.get(['isTwitterAccount'])!.value,
      image: this.editForm.get(['image'])!.value,
      rate: this.editForm.get(['rate'])!.value,
      fiveStar: this.editForm.get(['fiveStar'])!.value,
      fourSatr: this.editForm.get(['fourSatr'])!.value,
      threeStar: this.editForm.get(['threeStar'])!.value,
      twoStar: this.editForm.get(['twoStar'])!.value,
      oneStar: this.editForm.get(['oneStar'])!.value,
      detalis: this.editForm.get(['detalis'])!.value,
      connection: this.editForm.get(['connection'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
