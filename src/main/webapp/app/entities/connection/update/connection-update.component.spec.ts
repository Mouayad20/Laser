import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConnectionService } from '../service/connection.service';
import { IConnection, Connection } from '../connection.model';

import { ConnectionUpdateComponent } from './connection-update.component';

describe('Connection Management Update Component', () => {
  let comp: ConnectionUpdateComponent;
  let fixture: ComponentFixture<ConnectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let connectionService: ConnectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConnectionUpdateComponent],
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
      .overrideTemplate(ConnectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConnectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    connectionService = TestBed.inject(ConnectionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const connection: IConnection = { id: 456 };

      activatedRoute.data = of({ connection });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(connection));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Connection>>();
      const connection = { id: 123 };
      jest.spyOn(connectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ connection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: connection }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(connectionService.update).toHaveBeenCalledWith(connection);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Connection>>();
      const connection = new Connection();
      jest.spyOn(connectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ connection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: connection }));
      saveSubject.complete();

      // THEN
      expect(connectionService.create).toHaveBeenCalledWith(connection);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Connection>>();
      const connection = { id: 123 };
      jest.spyOn(connectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ connection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(connectionService.update).toHaveBeenCalledWith(connection);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
