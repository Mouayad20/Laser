import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AccountProviderService } from '../service/account-provider.service';
import { IAccountProvider, AccountProvider } from '../account-provider.model';

import { AccountProviderUpdateComponent } from './account-provider-update.component';

describe('AccountProvider Management Update Component', () => {
  let comp: AccountProviderUpdateComponent;
  let fixture: ComponentFixture<AccountProviderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountProviderService: AccountProviderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AccountProviderUpdateComponent],
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
      .overrideTemplate(AccountProviderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountProviderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountProviderService = TestBed.inject(AccountProviderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const accountProvider: IAccountProvider = { id: 456 };

      activatedRoute.data = of({ accountProvider });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(accountProvider));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountProvider>>();
      const accountProvider = { id: 123 };
      jest.spyOn(accountProviderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountProvider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountProvider }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountProviderService.update).toHaveBeenCalledWith(accountProvider);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountProvider>>();
      const accountProvider = new AccountProvider();
      jest.spyOn(accountProviderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountProvider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountProvider }));
      saveSubject.complete();

      // THEN
      expect(accountProviderService.create).toHaveBeenCalledWith(accountProvider);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountProvider>>();
      const accountProvider = { id: 123 };
      jest.spyOn(accountProviderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountProvider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountProviderService.update).toHaveBeenCalledWith(accountProvider);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
