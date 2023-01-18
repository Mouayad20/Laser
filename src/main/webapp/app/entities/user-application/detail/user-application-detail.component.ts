import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserApplication } from '../user-application.model';

@Component({
  selector: 'jhi-user-application-detail',
  templateUrl: './user-application-detail.component.html',
})
export class UserApplicationDetailComponent implements OnInit {
  userApplication: IUserApplication | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userApplication }) => {
      this.userApplication = userApplication;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
