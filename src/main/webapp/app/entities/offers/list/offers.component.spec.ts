import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OffersService } from '../service/offers.service';

import { OffersComponent } from './offers.component';

describe('Offers Management Component', () => {
  let comp: OffersComponent;
  let fixture: ComponentFixture<OffersComponent>;
  let service: OffersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OffersComponent],
    })
      .overrideTemplate(OffersComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OffersComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OffersService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.offers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
