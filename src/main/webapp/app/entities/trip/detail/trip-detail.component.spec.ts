import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TripDetailComponent } from './trip-detail.component';

describe('Trip Management Detail Component', () => {
  let comp: TripDetailComponent;
  let fixture: ComponentFixture<TripDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TripDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ trip: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TripDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TripDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load trip on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.trip).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
