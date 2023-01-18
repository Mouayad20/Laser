import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConstantsService } from '../service/constants.service';
import { IConstants, Constants } from '../constants.model';

import { ConstantsUpdateComponent } from './constants-update.component';

describe('Constants Management Update Component', () => {
  let comp: ConstantsUpdateComponent;
  let fixture: ComponentFixture<ConstantsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let constantsService: ConstantsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConstantsUpdateComponent],
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
      .overrideTemplate(ConstantsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConstantsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    constantsService = TestBed.inject(ConstantsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const constants: IConstants = { id: 456 };

      activatedRoute.data = of({ constants });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(constants));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Constants>>();
      const constants = { id: 123 };
      jest.spyOn(constantsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ constants });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: constants }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(constantsService.update).toHaveBeenCalledWith(constants);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Constants>>();
      const constants = new Constants();
      jest.spyOn(constantsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ constants });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: constants }));
      saveSubject.complete();

      // THEN
      expect(constantsService.create).toHaveBeenCalledWith(constants);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Constants>>();
      const constants = { id: 123 };
      jest.spyOn(constantsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ constants });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(constantsService.update).toHaveBeenCalledWith(constants);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
