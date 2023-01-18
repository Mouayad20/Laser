import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAccountProvider } from '../account-provider.model';

@Component({
  selector: 'jhi-account-provider-detail',
  templateUrl: './account-provider-detail.component.html',
})
export class AccountProviderDetailComponent implements OnInit {
  accountProvider: IAccountProvider | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountProvider }) => {
      this.accountProvider = accountProvider;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
