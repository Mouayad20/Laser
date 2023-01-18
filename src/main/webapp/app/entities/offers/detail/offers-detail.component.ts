import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOffers } from '../offers.model';

@Component({
  selector: 'jhi-offers-detail',
  templateUrl: './offers-detail.component.html',
})
export class OffersDetailComponent implements OnInit {
  offers: IOffers | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ offers }) => {
      this.offers = offers;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
