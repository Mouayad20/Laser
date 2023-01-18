import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ConstantsService } from '../service/constants.service';

import { ConstantsComponent } from './constants.component';

describe('Constants Management Component', () => {
  let comp: ConstantsComponent;
  let fixture: ComponentFixture<ConstantsComponent>;
  let service: ConstantsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ConstantsComponent],
    })
      .overrideTemplate(ConstantsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConstantsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ConstantsService);

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
    expect(comp.constants?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
