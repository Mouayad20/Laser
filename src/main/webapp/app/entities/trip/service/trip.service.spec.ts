import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITrip, Trip } from '../trip.model';

import { TripService } from './trip.service';

describe('Trip Service', () => {
  let service: TripService;
  let httpMock: HttpTestingController;
  let elemDefault: ITrip;
  let expectedResult: ITrip | ITrip[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TripService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      createdAt: currentDate,
      flyTime: currentDate,
      arriveTime: currentDate,
      tripIdentifier: 'AAAAAAA',
      details: 'AAAAAAA',
      ticketImage: 'AAAAAAA',
      tripType: 'AAAAAAA',
      transit: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          flyTime: currentDate.format(DATE_TIME_FORMAT),
          arriveTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Trip', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          flyTime: currentDate.format(DATE_TIME_FORMAT),
          arriveTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
          flyTime: currentDate,
          arriveTime: currentDate,
        },
        returnedFromService
      );

      service.create(new Trip()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Trip', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          flyTime: currentDate.format(DATE_TIME_FORMAT),
          arriveTime: currentDate.format(DATE_TIME_FORMAT),
          tripIdentifier: 'BBBBBB',
          details: 'BBBBBB',
          ticketImage: 'BBBBBB',
          tripType: 'BBBBBB',
          transit: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
          flyTime: currentDate,
          arriveTime: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Trip', () => {
      const patchObject = Object.assign(
        {
          flyTime: currentDate.format(DATE_TIME_FORMAT),
          arriveTime: currentDate.format(DATE_TIME_FORMAT),
          tripType: 'BBBBBB',
        },
        new Trip()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createdAt: currentDate,
          flyTime: currentDate,
          arriveTime: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Trip', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          flyTime: currentDate.format(DATE_TIME_FORMAT),
          arriveTime: currentDate.format(DATE_TIME_FORMAT),
          tripIdentifier: 'BBBBBB',
          details: 'BBBBBB',
          ticketImage: 'BBBBBB',
          tripType: 'BBBBBB',
          transit: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
          flyTime: currentDate,
          arriveTime: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Trip', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTripToCollectionIfMissing', () => {
      it('should add a Trip to an empty array', () => {
        const trip: ITrip = { id: 123 };
        expectedResult = service.addTripToCollectionIfMissing([], trip);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trip);
      });

      it('should not add a Trip to an array that contains it', () => {
        const trip: ITrip = { id: 123 };
        const tripCollection: ITrip[] = [
          {
            ...trip,
          },
          { id: 456 },
        ];
        expectedResult = service.addTripToCollectionIfMissing(tripCollection, trip);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Trip to an array that doesn't contain it", () => {
        const trip: ITrip = { id: 123 };
        const tripCollection: ITrip[] = [{ id: 456 }];
        expectedResult = service.addTripToCollectionIfMissing(tripCollection, trip);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trip);
      });

      it('should add only unique Trip to an array', () => {
        const tripArray: ITrip[] = [{ id: 123 }, { id: 456 }, { id: 23550 }];
        const tripCollection: ITrip[] = [{ id: 123 }];
        expectedResult = service.addTripToCollectionIfMissing(tripCollection, ...tripArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trip: ITrip = { id: 123 };
        const trip2: ITrip = { id: 456 };
        expectedResult = service.addTripToCollectionIfMissing([], trip, trip2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trip);
        expect(expectedResult).toContain(trip2);
      });

      it('should accept null and undefined values', () => {
        const trip: ITrip = { id: 123 };
        expectedResult = service.addTripToCollectionIfMissing([], null, trip, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trip);
      });

      it('should return initial array if no Trip is added', () => {
        const tripCollection: ITrip[] = [{ id: 123 }];
        expectedResult = service.addTripToCollectionIfMissing(tripCollection, undefined, null);
        expect(expectedResult).toEqual(tripCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
