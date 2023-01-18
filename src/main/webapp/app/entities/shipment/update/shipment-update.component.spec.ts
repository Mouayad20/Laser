import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ShipmentService } from '../service/shipment.service';
import { IShipment, Shipment } from '../shipment.model';
import { IShipmentType } from 'app/entities/shipment-type/shipment-type.model';
import { ShipmentTypeService } from 'app/entities/shipment-type/service/shipment-type.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { IDeal } from 'app/entities/deal/deal.model';
import { DealService } from 'app/entities/deal/service/deal.service';

import { ShipmentUpdateComponent } from './shipment-update.component';

describe('Shipment Management Update Component', () => {
  let comp: ShipmentUpdateComponent;
  let fixture: ComponentFixture<ShipmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let shipmentService: ShipmentService;
  let shipmentTypeService: ShipmentTypeService;
  let locationService: LocationService;
  let dealService: DealService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ShipmentUpdateComponent],
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
      .overrideTemplate(ShipmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ShipmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shipmentService = TestBed.inject(ShipmentService);
    shipmentTypeService = TestBed.inject(ShipmentTypeService);
    locationService = TestBed.inject(LocationService);
    dealService = TestBed.inject(DealService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ShipmentType query and add missing value', () => {
      const shipment: IShipment = { id: 456 };
      const type: IShipmentType = { id: 85126 };
      shipment.type = type;

      const shipmentTypeCollection: IShipmentType[] = [{ id: 40120 }];
      jest.spyOn(shipmentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentTypeCollection })));
      const additionalShipmentTypes = [type];
      const expectedCollection: IShipmentType[] = [...additionalShipmentTypes, ...shipmentTypeCollection];
      jest.spyOn(shipmentTypeService, 'addShipmentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      expect(shipmentTypeService.query).toHaveBeenCalled();
      expect(shipmentTypeService.addShipmentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentTypeCollection,
        ...additionalShipmentTypes
      );
      expect(comp.shipmentTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Location query and add missing value', () => {
      const shipment: IShipment = { id: 456 };
      const to: ILocation = { id: 23305 };
      shipment.to = to;
      const from: ILocation = { id: 14539 };
      shipment.from = from;

      const locationCollection: ILocation[] = [{ id: 52515 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const additionalLocations = [to, from];
      const expectedCollection: ILocation[] = [...additionalLocations, ...locationCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(locationCollection, ...additionalLocations);
      expect(comp.locationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Deal query and add missing value', () => {
      const shipment: IShipment = { id: 456 };
      const deal: IDeal = { id: 90401 };
      shipment.deal = deal;

      const dealCollection: IDeal[] = [{ id: 49568 }];
      jest.spyOn(dealService, 'query').mockReturnValue(of(new HttpResponse({ body: dealCollection })));
      const additionalDeals = [deal];
      const expectedCollection: IDeal[] = [...additionalDeals, ...dealCollection];
      jest.spyOn(dealService, 'addDealToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      expect(dealService.query).toHaveBeenCalled();
      expect(dealService.addDealToCollectionIfMissing).toHaveBeenCalledWith(dealCollection, ...additionalDeals);
      expect(comp.dealsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const shipment: IShipment = { id: 456 };
      const type: IShipmentType = { id: 81981 };
      shipment.type = type;
      const to: ILocation = { id: 24898 };
      shipment.to = to;
      const from: ILocation = { id: 74889 };
      shipment.from = from;
      const deal: IDeal = { id: 32033 };
      shipment.deal = deal;

      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(shipment));
      expect(comp.shipmentTypesSharedCollection).toContain(type);
      expect(comp.locationsSharedCollection).toContain(to);
      expect(comp.locationsSharedCollection).toContain(from);
      expect(comp.dealsSharedCollection).toContain(deal);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Shipment>>();
      const shipment = { id: 123 };
      jest.spyOn(shipmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shipment }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(shipmentService.update).toHaveBeenCalledWith(shipment);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Shipment>>();
      const shipment = new Shipment();
      jest.spyOn(shipmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shipment }));
      saveSubject.complete();

      // THEN
      expect(shipmentService.create).toHaveBeenCalledWith(shipment);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Shipment>>();
      const shipment = { id: 123 };
      jest.spyOn(shipmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shipmentService.update).toHaveBeenCalledWith(shipment);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackShipmentTypeById', () => {
      it('Should return tracked ShipmentType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackShipmentTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackLocationById', () => {
      it('Should return tracked Location primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLocationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDealById', () => {
      it('Should return tracked Deal primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDealById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
