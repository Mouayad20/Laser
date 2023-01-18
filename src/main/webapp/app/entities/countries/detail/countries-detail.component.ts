import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICountries } from '../countries.model';

@Component({
  selector: 'jhi-countries-detail',
  templateUrl: './countries-detail.component.html',
})
export class CountriesDetailComponent implements OnInit {
  countries: ICountries | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countries }) => {
      this.countries = countries;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
