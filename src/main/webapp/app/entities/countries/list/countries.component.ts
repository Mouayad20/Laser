import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICountries } from '../countries.model';
import { CountriesService } from '../service/countries.service';
import { CountriesDeleteDialogComponent } from '../delete/countries-delete-dialog.component';

@Component({
  selector: 'jhi-countries',
  templateUrl: './countries.component.html',
})
export class CountriesComponent implements OnInit {
  countries?: ICountries[];
  isLoading = false;

  constructor(protected countriesService: CountriesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.countriesService.query().subscribe({
      next: (res: HttpResponse<ICountries[]>) => {
        this.isLoading = false;
        this.countries = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICountries): number {
    return item.id!;
  }

  delete(countries: ICountries): void {
    const modalRef = this.modalService.open(CountriesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.countries = countries;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
