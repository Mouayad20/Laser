import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ShipmentTypeService } from '../service/shipment-type.service';
import { IShipmentType, ShipmentType } from '../shipment-type.model';

import { ShipmentTypeUpdateComponent } from './shipment-type-update.component';

describe('ShipmentType Management Update Component', () => {
  let comp: ShipmentTypeUpdateComponent;
  let fixture: ComponentFixture<ShipmentTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let shipmentTypeService: ShipmentTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ShipmentTypeUpdateComponent],
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
      .overrideTemplate(ShipmentTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ShipmentTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shipmentTypeService = TestBed.inject(ShipmentTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const shipmentType: IShipmentType = { id: 456 };

      activatedRoute.data = of({ shipmentType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(shipmentType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ShipmentType>>();
      const shipmentType = { id: 123 };
      jest.spyOn(shipmentTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shipmentType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(shipmentTypeService.update).toHaveBeenCalledWith(shipmentType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ShipmentType>>();
      const shipmentType = new ShipmentType();
      jest.spyOn(shipmentTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shipmentType }));
      saveSubject.complete();

      // THEN
      expect(shipmentTypeService.create).toHaveBeenCalledWith(shipmentType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ShipmentType>>();
      const shipmentType = { id: 123 };
      jest.spyOn(shipmentTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shipmentTypeService.update).toHaveBeenCalledWith(shipmentType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
