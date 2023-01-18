import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UserApplicationService } from '../service/user-application.service';
import { IUserApplication, UserApplication } from '../user-application.model';
import { IConnection } from 'app/entities/connection/connection.model';
import { ConnectionService } from 'app/entities/connection/service/connection.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { UserApplicationUpdateComponent } from './user-application-update.component';

describe('UserApplication Management Update Component', () => {
  let comp: UserApplicationUpdateComponent;
  let fixture: ComponentFixture<UserApplicationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userApplicationService: UserApplicationService;
  let connectionService: ConnectionService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UserApplicationUpdateComponent],
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
      .overrideTemplate(UserApplicationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserApplicationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userApplicationService = TestBed.inject(UserApplicationService);
    connectionService = TestBed.inject(ConnectionService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call connection query and add missing value', () => {
      const userApplication: IUserApplication = { id: 456 };
      const connection: IConnection = { id: 59898 };
      userApplication.connection = connection;

      const connectionCollection: IConnection[] = [{ id: 14999 }];
      jest.spyOn(connectionService, 'query').mockReturnValue(of(new HttpResponse({ body: connectionCollection })));
      const expectedCollection: IConnection[] = [connection, ...connectionCollection];
      jest.spyOn(connectionService, 'addConnectionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userApplication });
      comp.ngOnInit();

      expect(connectionService.query).toHaveBeenCalled();
      expect(connectionService.addConnectionToCollectionIfMissing).toHaveBeenCalledWith(connectionCollection, connection);
      expect(comp.connectionsCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const userApplication: IUserApplication = { id: 456 };
      const user: IUser = { id: 70368 };
      userApplication.user = user;

      const userCollection: IUser[] = [{ id: 29219 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userApplication });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userApplication: IUserApplication = { id: 456 };
      const connection: IConnection = { id: 19637 };
      userApplication.connection = connection;
      const user: IUser = { id: 11052 };
      userApplication.user = user;

      activatedRoute.data = of({ userApplication });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(userApplication));
      expect(comp.connectionsCollection).toContain(connection);
      expect(comp.usersSharedCollection).toContain(user);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UserApplication>>();
      const userApplication = { id: 123 };
      jest.spyOn(userApplicationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userApplication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userApplication }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(userApplicationService.update).toHaveBeenCalledWith(userApplication);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UserApplication>>();
      const userApplication = new UserApplication();
      jest.spyOn(userApplicationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userApplication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userApplication }));
      saveSubject.complete();

      // THEN
      expect(userApplicationService.create).toHaveBeenCalledWith(userApplication);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UserApplication>>();
      const userApplication = { id: 123 };
      jest.spyOn(userApplicationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userApplication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userApplicationService.update).toHaveBeenCalledWith(userApplication);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackConnectionById', () => {
      it('Should return tracked Connection primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackConnectionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
