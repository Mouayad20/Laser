import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOffers } from '../offers.model';
import { OffersService } from '../service/offers.service';
import { OffersDeleteDialogComponent } from '../delete/offers-delete-dialog.component';

@Component({
  selector: 'jhi-offers',
  templateUrl: './offers.component.html',
})
export class OffersComponent implements OnInit {
  offers?: IOffers[];
  isLoading = false;

  constructor(protected offersService: OffersService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.offersService.query().subscribe({
      next: (res: HttpResponse<IOffers[]>) => {
        this.isLoading = false;
        this.offers = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IOffers): number {
    return item.id!;
  }

  delete(offers: IOffers): void {
    const modalRef = this.modalService.open(OffersDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.offers = offers;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
