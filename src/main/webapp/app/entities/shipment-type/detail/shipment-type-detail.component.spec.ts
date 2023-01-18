import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ShipmentTypeDetailComponent } from './shipment-type-detail.component';

describe('ShipmentType Management Detail Component', () => {
  let comp: ShipmentTypeDetailComponent;
  let fixture: ComponentFixture<ShipmentTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShipmentTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ shipmentType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ShipmentTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ShipmentTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load shipmentType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.shipmentType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
