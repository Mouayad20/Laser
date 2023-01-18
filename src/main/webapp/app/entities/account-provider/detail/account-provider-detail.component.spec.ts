import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccountProviderDetailComponent } from './account-provider-detail.component';

describe('AccountProvider Management Detail Component', () => {
  let comp: AccountProviderDetailComponent;
  let fixture: ComponentFixture<AccountProviderDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccountProviderDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ accountProvider: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AccountProviderDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AccountProviderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load accountProvider on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.accountProvider).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
