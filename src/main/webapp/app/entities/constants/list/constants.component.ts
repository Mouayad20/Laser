import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IConstants } from '../constants.model';
import { ConstantsService } from '../service/constants.service';
import { ConstantsDeleteDialogComponent } from '../delete/constants-delete-dialog.component';

@Component({
  selector: 'jhi-constants',
  templateUrl: './constants.component.html',
})
export class ConstantsComponent implements OnInit {
  constants?: IConstants[];
  isLoading = false;

  constructor(protected constantsService: ConstantsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.constantsService.query().subscribe({
      next: (res: HttpResponse<IConstants[]>) => {
        this.isLoading = false;
        this.constants = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IConstants): number {
    return item.id!;
  }

  delete(constants: IConstants): void {
    const modalRef = this.modalService.open(ConstantsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.constants = constants;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
