import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConstants } from '../constants.model';

@Component({
  selector: 'jhi-constants-detail',
  templateUrl: './constants-detail.component.html',
})
export class ConstantsDetailComponent implements OnInit {
  constants: IConstants | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ constants }) => {
      this.constants = constants;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
