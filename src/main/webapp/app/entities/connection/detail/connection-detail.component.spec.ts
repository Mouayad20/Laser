import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConnectionDetailComponent } from './connection-detail.component';

describe('Connection Management Detail Component', () => {
  let comp: ConnectionDetailComponent;
  let fixture: ComponentFixture<ConnectionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConnectionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ connection: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ConnectionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ConnectionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load connection on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.connection).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
