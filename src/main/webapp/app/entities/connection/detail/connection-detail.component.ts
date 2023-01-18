import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConnection } from '../connection.model';

@Component({
  selector: 'jhi-connection-detail',
  templateUrl: './connection-detail.component.html',
})
export class ConnectionDetailComponent implements OnInit {
  connection: IConnection | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ connection }) => {
      this.connection = connection;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
