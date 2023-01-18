import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DealDetailComponent } from './deal-detail.component';

describe('Deal Management Detail Component', () => {
  let comp: DealDetailComponent;
  let fixture: ComponentFixture<DealDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DealDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deal: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DealDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DealDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deal on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deal).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
