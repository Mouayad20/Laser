import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DealStatusService } from '../service/deal-status.service';
import { IDealStatus, DealStatus } from '../deal-status.model';

import { DealStatusUpdateComponent } from './deal-status-update.component';

describe('DealStatus Management Update Component', () => {
  let comp: DealStatusUpdateComponent;
  let fixture: ComponentFixture<DealStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dealStatusService: DealStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DealStatusUpdateComponent],
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
      .overrideTemplate(DealStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DealStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dealStatusService = TestBed.inject(DealStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const dealStatus: IDealStatus = { id: 456 };

      activatedRoute.data = of({ dealStatus });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(dealStatus));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DealStatus>>();
      const dealStatus = { id: 123 };
      jest.spyOn(dealStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dealStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dealStatus }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(dealStatusService.update).toHaveBeenCalledWith(dealStatus);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DealStatus>>();
      const dealStatus = new DealStatus();
      jest.spyOn(dealStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dealStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dealStatus }));
      saveSubject.complete();

      // THEN
      expect(dealStatusService.create).toHaveBeenCalledWith(dealStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DealStatus>>();
      const dealStatus = { id: 123 };
      jest.spyOn(dealStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dealStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dealStatusService.update).toHaveBeenCalledWith(dealStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
