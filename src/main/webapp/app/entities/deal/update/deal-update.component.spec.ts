import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DealService } from '../service/deal.service';
import { IDeal, Deal } from '../deal.model';
import { ITransaction } from 'app/entities/transaction/transaction.model';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';
import { IUserApplication } from 'app/entities/user-application/user-application.model';
import { UserApplicationService } from 'app/entities/user-application/service/user-application.service';
import { ITrip } from 'app/entities/trip/trip.model';
import { TripService } from 'app/entities/trip/service/trip.service';
import { IDealStatus } from 'app/entities/deal-status/deal-status.model';
import { DealStatusService } from 'app/entities/deal-status/service/deal-status.service';

import { DealUpdateComponent } from './deal-update.component';

describe('Deal Management Update Component', () => {
  let comp: DealUpdateComponent;
  let fixture: ComponentFixture<DealUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dealService: DealService;
  let transactionService: TransactionService;
  let userApplicationService: UserApplicationService;
  let tripService: TripService;
  let dealStatusService: DealStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DealUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DealUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DealUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dealService = TestBed.inject(DealService);
    transactionService = TestBed.inject(TransactionService);
    userApplicationService = TestBed.inject(UserApplicationService);
    tripService = TestBed.inject(TripService);
    dealStatusService = TestBed.inject(DealStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call transaction query and add missing value', () => {
      const deal: IDeal = { id: 456 };
      const transaction: ITransaction = { id: 80247 };
      deal.transaction = transaction;

      const transactionCollection: ITransaction[] = [{ id: 58220 }];
      jest.spyOn(transactionService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionCollection })));
      const expectedCollection: ITransaction[] = [transaction, ...transactionCollection];
      jest.spyOn(transactionService, 'addTransactionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deal });
      comp.ngOnInit();

      expect(transactionService.query).toHaveBeenCalled();
      expect(transactionService.addTransactionToCollectionIfMissing).toHaveBeenCalledWith(transactionCollection, transaction);
      expect(comp.transactionsCollection).toEqual(expectedCollection);
    });

    it('Should call UserApplication query and add missing value', () => {
      const deal: IDeal = { id: 456 };
      const deliver: IUserApplication = { id: 63966 };
      deal.deliver = deliver;
      const owner: IUserApplication = { id: 49581 };
      deal.owner = owner;

      const userApplicationCollection: IUserApplication[] = [{ id: 91879 }];
      jest.spyOn(userApplicationService, 'query').mockReturnValue(of(new HttpResponse({ body: userApplicationCollection })));
      const additionalUserApplications = [deliver, owner];
      const expectedCollection: IUserApplication[] = [...additionalUserApplications, ...userApplicationCollection];
      jest.spyOn(userApplicationService, 'addUserApplicationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deal });
      comp.ngOnInit();

      expect(userApplicationService.query).toHaveBeenCalled();
      expect(userApplicationService.addUserApplicationToCollectionIfMissing).toHaveBeenCalledWith(
        userApplicationCollection,
        ...additionalUserApplications
      );
      expect(comp.userApplicationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Trip query and add missing value', () => {
      const deal: IDeal = { id: 456 };
      const trip: ITrip = { id: 52585 };
      deal.trip = trip;

      const tripCollection: ITrip[] = [{ id: 44396 }];
      jest.spyOn(tripService, 'query').mockReturnValue(of(new HttpResponse({ body: tripCollection })));
      const additionalTrips = [trip];
      const expectedCollection: ITrip[] = [...additionalTrips, ...tripCollection];
      jest.spyOn(tripService, 'addTripToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deal });
      comp.ngOnInit();

      expect(tripService.query).toHaveBeenCalled();
      expect(tripService.addTripToCollectionIfMissing).toHaveBeenCalledWith(tripCollection, ...additionalTrips);
      expect(comp.tripsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DealStatus query and add missing value', () => {
      const deal: IDeal = { id: 456 };
      const status: IDealStatus = { id: 47309 };
      deal.status = status;

      const dealStatusCollection: IDealStatus[] = [{ id: 633 }];
      jest.spyOn(dealStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: dealStatusCollection })));
      const additionalDealStatuses = [status];
      const expectedCollection: IDealStatus[] = [...additionalDealStatuses, ...dealStatusCollection];
      jest.spyOn(dealStatusService, 'addDealStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deal });
      comp.ngOnInit();

      expect(dealStatusService.query).toHaveBeenCalled();
      expect(dealStatusService.addDealStatusToCollectionIfMissing).toHaveBeenCalledWith(dealStatusCollection, ...additionalDealStatuses);
      expect(comp.dealStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const deal: IDeal = { id: 456 };
      const transaction: ITransaction = { id: 21189 };
      deal.transaction = transaction;
      const deliver: IUserApplication = { id: 12313 };
      deal.deliver = deliver;
      const owner: IUserApplication = { id: 51145 };
      deal.owner = owner;
      const trip: ITrip = { id: 36676 };
      deal.trip = trip;
      const status: IDealStatus = { id: 69538 };
      deal.status = status;

      activatedRoute.data = of({ deal });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(deal));
      expect(comp.transactionsCollection).toContain(transaction);
      expect(comp.userApplicationsSharedCollection).toContain(deliver);
      expect(comp.userApplicationsSharedCollection).toContain(owner);
      expect(comp.tripsSharedCollection).toContain(trip);
      expect(comp.dealStatusesSharedCollection).toContain(status);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deal>>();
      const deal = { id: 123 };
      jest.spyOn(dealService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deal }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(dealService.update).toHaveBeenCalledWith(deal);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deal>>();
      const deal = new Deal();
      jest.spyOn(dealService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deal }));
      saveSubject.complete();

      // THEN
      expect(dealService.create).toHaveBeenCalledWith(deal);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deal>>();
      const deal = { id: 123 };
      jest.spyOn(dealService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dealService.update).toHaveBeenCalledWith(deal);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTransactionById', () => {
      it('Should return tracked Transaction primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTransactionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUserApplicationById', () => {
      it('Should return tracked UserApplication primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserApplicationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTripById', () => {
      it('Should return tracked Trip primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTripById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDealStatusById', () => {
      it('Should return tracked DealStatus primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDealStatusById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
