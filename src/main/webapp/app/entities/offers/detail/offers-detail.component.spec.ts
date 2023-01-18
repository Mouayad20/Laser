import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OffersDetailComponent } from './offers-detail.component';

describe('Offers Management Detail Component', () => {
  let comp: OffersDetailComponent;
  let fixture: ComponentFixture<OffersDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OffersDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ offers: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OffersDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OffersDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load offers on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.offers).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
