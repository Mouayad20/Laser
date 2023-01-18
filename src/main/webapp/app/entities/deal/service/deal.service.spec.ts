import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDeal, Deal } from '../deal.model';

import { DealService } from './deal.service';

describe('Deal Service', () => {
  let service: DealService;
  let httpMock: HttpTestingController;
  let elemDefault: IDeal;
  let expectedResult: IDeal | IDeal[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DealService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      totalPrice: 0,
      isCashed: false,
      fromAccount: 'AAAAAAA',
      toAccount: 'AAAAAAA',
      fullWeight: 0,
      availableWeight: 0,
      arrivelDate: currentDate,
      expectedDate: currentDate,
      details: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          arrivelDate: currentDate.format(DATE_TIME_FORMAT),
          expectedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Deal', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          arrivelDate: currentDate.format(DATE_TIME_FORMAT),
          expectedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          arrivelDate: currentDate,
          expectedDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Deal()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Deal', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          totalPrice: 1,
          isCashed: true,
          fromAccount: 'BBBBBB',
          toAccount: 'BBBBBB',
          fullWeight: 1,
          availableWeight: 1,
          arrivelDate: currentDate.format(DATE_TIME_FORMAT),
          expectedDate: currentDate.format(DATE_TIME_FORMAT),
          details: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          arrivelDate: currentDate,
          expectedDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Deal', () => {
      const patchObject = Object.assign(
        {
          fullWeight: 1,
          arrivelDate: currentDate.format(DATE_TIME_FORMAT),
          expectedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        new Deal()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          arrivelDate: currentDate,
          expectedDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Deal', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          totalPrice: 1,
          isCashed: true,
          fromAccount: 'BBBBBB',
          toAccount: 'BBBBBB',
          fullWeight: 1,
          availableWeight: 1,
          arrivelDate: currentDate.format(DATE_TIME_FORMAT),
          expectedDate: currentDate.format(DATE_TIME_FORMAT),
          details: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          arrivelDate: currentDate,
          expectedDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Deal', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDealToCollectionIfMissing', () => {
      it('should add a Deal to an empty array', () => {
        const deal: IDeal = { id: 123 };
        expectedResult = service.addDealToCollectionIfMissing([], deal);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deal);
      });

      it('should not add a Deal to an array that contains it', () => {
        const deal: IDeal = { id: 123 };
        const dealCollection: IDeal[] = [
          {
            ...deal,
          },
          { id: 456 },
        ];
        expectedResult = service.addDealToCollectionIfMissing(dealCollection, deal);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Deal to an array that doesn't contain it", () => {
        const deal: IDeal = { id: 123 };
        const dealCollection: IDeal[] = [{ id: 456 }];
        expectedResult = service.addDealToCollectionIfMissing(dealCollection, deal);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deal);
      });

      it('should add only unique Deal to an array', () => {
        const dealArray: IDeal[] = [{ id: 123 }, { id: 456 }, { id: 91403 }];
        const dealCollection: IDeal[] = [{ id: 123 }];
        expectedResult = service.addDealToCollectionIfMissing(dealCollection, ...dealArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deal: IDeal = { id: 123 };
        const deal2: IDeal = { id: 456 };
        expectedResult = service.addDealToCollectionIfMissing([], deal, deal2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deal);
        expect(expectedResult).toContain(deal2);
      });

      it('should accept null and undefined values', () => {
        const deal: IDeal = { id: 123 };
        expectedResult = service.addDealToCollectionIfMissing([], null, deal, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deal);
      });

      it('should return initial array if no Deal is added', () => {
        const dealCollection: IDeal[] = [{ id: 123 }];
        expectedResult = service.addDealToCollectionIfMissing(dealCollection, undefined, null);
        expect(expectedResult).toEqual(dealCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
