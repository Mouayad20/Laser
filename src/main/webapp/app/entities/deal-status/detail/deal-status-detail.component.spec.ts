import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DealStatusDetailComponent } from './deal-status-detail.component';

describe('DealStatus Management Detail Component', () => {
  let comp: DealStatusDetailComponent;
  let fixture: ComponentFixture<DealStatusDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DealStatusDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dealStatus: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DealStatusDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DealStatusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dealStatus on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dealStatus).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
