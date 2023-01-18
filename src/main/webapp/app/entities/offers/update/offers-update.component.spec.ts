import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OffersService } from '../service/offers.service';
import { IOffers, Offers } from '../offers.model';

import { OffersUpdateComponent } from './offers-update.component';

describe('Offers Management Update Component', () => {
  let comp: OffersUpdateComponent;
  let fixture: ComponentFixture<OffersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let offersService: OffersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OffersUpdateComponent],
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
      .overrideTemplate(OffersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OffersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    offersService = TestBed.inject(OffersService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const offers: IOffers = { id: 456 };

      activatedRoute.data = of({ offers });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(offers));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Offers>>();
      const offers = { id: 123 };
      jest.spyOn(offersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ offers });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: offers }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(offersService.update).toHaveBeenCalledWith(offers);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Offers>>();
      const offers = new Offers();
      jest.spyOn(offersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ offers });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: offers }));
      saveSubject.complete();

      // THEN
      expect(offersService.create).toHaveBeenCalledWith(offers);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Offers>>();
      const offers = { id: 123 };
      jest.spyOn(offersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ offers });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(offersService.update).toHaveBeenCalledWith(offers);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
