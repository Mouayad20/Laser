import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CountriesDetailComponent } from './countries-detail.component';

describe('Countries Management Detail Component', () => {
  let comp: CountriesDetailComponent;
  let fixture: ComponentFixture<CountriesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CountriesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ countries: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CountriesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CountriesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load countries on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.countries).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
