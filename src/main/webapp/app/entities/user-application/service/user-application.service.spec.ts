import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserApplication, UserApplication } from '../user-application.model';

import { UserApplicationService } from './user-application.service';

describe('UserApplication Service', () => {
  let service: UserApplicationService;
  let httpMock: HttpTestingController;
  let elemDefault: IUserApplication;
  let expectedResult: IUserApplication | IUserApplication[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserApplicationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      phone: 'AAAAAAA',
      passport: 'AAAAAAA',
      createdAt: currentDate,
      isGoogleAccount: false,
      isFacebookAccount: false,
      isTwitterAccount: false,
      image: 'AAAAAAA',
      rate: 0,
      fiveStar: 0,
      fourSatr: 0,
      threeStar: 0,
      twoStar: 0,
      oneStar: 0,
      detalis: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createdAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a UserApplication', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
        },
        returnedFromService
      );

      service.create(new UserApplication()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserApplication', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          phone: 'BBBBBB',
          passport: 'BBBBBB',
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          isGoogleAccount: true,
          isFacebookAccount: true,
          isTwitterAccount: true,
          image: 'BBBBBB',
          rate: 1,
          fiveStar: 1,
          fourSatr: 1,
          threeStar: 1,
          twoStar: 1,
          oneStar: 1,
          detalis: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserApplication', () => {
      const patchObject = Object.assign(
        {
          phone: 'BBBBBB',
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          isFacebookAccount: true,
          image: 'BBBBBB',
          rate: 1,
          fiveStar: 1,
          twoStar: 1,
          oneStar: 1,
          detalis: 'BBBBBB',
        },
        new UserApplication()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createdAt: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserApplication', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          phone: 'BBBBBB',
          passport: 'BBBBBB',
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          isGoogleAccount: true,
          isFacebookAccount: true,
          isTwitterAccount: true,
          image: 'BBBBBB',
          rate: 1,
          fiveStar: 1,
          fourSatr: 1,
          threeStar: 1,
          twoStar: 1,
          oneStar: 1,
          detalis: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a UserApplication', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUserApplicationToCollectionIfMissing', () => {
      it('should add a UserApplication to an empty array', () => {
        const userApplication: IUserApplication = { id: 123 };
        expectedResult = service.addUserApplicationToCollectionIfMissing([], userApplication);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userApplication);
      });

      it('should not add a UserApplication to an array that contains it', () => {
        const userApplication: IUserApplication = { id: 123 };
        const userApplicationCollection: IUserApplication[] = [
          {
            ...userApplication,
          },
          { id: 456 },
        ];
        expectedResult = service.addUserApplicationToCollectionIfMissing(userApplicationCollection, userApplication);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserApplication to an array that doesn't contain it", () => {
        const userApplication: IUserApplication = { id: 123 };
        const userApplicationCollection: IUserApplication[] = [{ id: 456 }];
        expectedResult = service.addUserApplicationToCollectionIfMissing(userApplicationCollection, userApplication);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userApplication);
      });

      it('should add only unique UserApplication to an array', () => {
        const userApplicationArray: IUserApplication[] = [{ id: 123 }, { id: 456 }, { id: 46928 }];
        const userApplicationCollection: IUserApplication[] = [{ id: 123 }];
        expectedResult = service.addUserApplicationToCollectionIfMissing(userApplicationCollection, ...userApplicationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userApplication: IUserApplication = { id: 123 };
        const userApplication2: IUserApplication = { id: 456 };
        expectedResult = service.addUserApplicationToCollectionIfMissing([], userApplication, userApplication2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userApplication);
        expect(expectedResult).toContain(userApplication2);
      });

      it('should accept null and undefined values', () => {
        const userApplication: IUserApplication = { id: 123 };
        expectedResult = service.addUserApplicationToCollectionIfMissing([], null, userApplication, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userApplication);
      });

      it('should return initial array if no UserApplication is added', () => {
        const userApplicationCollection: IUserApplication[] = [{ id: 123 }];
        expectedResult = service.addUserApplicationToCollectionIfMissing(userApplicationCollection, undefined, null);
        expect(expectedResult).toEqual(userApplicationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
