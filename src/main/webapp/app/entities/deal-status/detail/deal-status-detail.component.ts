import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDealStatus } from '../deal-status.model';

@Component({
  selector: 'jhi-deal-status-detail',
  templateUrl: './deal-status-detail.component.html',
})
export class DealStatusDetailComponent implements OnInit {
  dealStatus: IDealStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dealStatus }) => {
      this.dealStatus = dealStatus;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
