import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConstantsDetailComponent } from './constants-detail.component';

describe('Constants Management Detail Component', () => {
  let comp: ConstantsDetailComponent;
  let fixture: ComponentFixture<ConstantsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConstantsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ constants: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ConstantsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ConstantsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load constants on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.constants).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
