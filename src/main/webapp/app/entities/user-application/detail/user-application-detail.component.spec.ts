import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UserApplicationDetailComponent } from './user-application-detail.component';

describe('UserApplication Management Detail Component', () => {
  let comp: UserApplicationDetailComponent;
  let fixture: ComponentFixture<UserApplicationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserApplicationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ userApplication: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UserApplicationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UserApplicationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userApplication on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.userApplication).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
