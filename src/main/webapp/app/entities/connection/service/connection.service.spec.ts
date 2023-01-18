import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IConnection, Connection } from '../connection.model';

import { ConnectionService } from './connection.service';

describe('Connection Service', () => {
  let service: ConnectionService;
  let httpMock: HttpTestingController;
  let elemDefault: IConnection;
  let expectedResult: IConnection | IConnection[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConnectionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fcmToken: 'AAAAAAA',
      localToken: 'AAAAAAA',
      localRefreshToken: 'AAAAAAA',
      oAuthToken: 'AAAAAAA',
      localTokenExpiryDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          localTokenExpiryDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Connection', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          localTokenExpiryDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          localTokenExpiryDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Connection()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Connection', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fcmToken: 'BBBBBB',
          localToken: 'BBBBBB',
          localRefreshToken: 'BBBBBB',
          oAuthToken: 'BBBBBB',
          localTokenExpiryDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          localTokenExpiryDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Connection', () => {
      const patchObject = Object.assign(
        {
          fcmToken: 'BBBBBB',
          localRefreshToken: 'BBBBBB',
        },
        new Connection()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          localTokenExpiryDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Connection', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fcmToken: 'BBBBBB',
          localToken: 'BBBBBB',
          localRefreshToken: 'BBBBBB',
          oAuthToken: 'BBBBBB',
          localTokenExpiryDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          localTokenExpiryDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Connection', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addConnectionToCollectionIfMissing', () => {
      it('should add a Connection to an empty array', () => {
        const connection: IConnection = { id: 123 };
        expectedResult = service.addConnectionToCollectionIfMissing([], connection);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(connection);
      });

      it('should not add a Connection to an array that contains it', () => {
        const connection: IConnection = { id: 123 };
        const connectionCollection: IConnection[] = [
          {
            ...connection,
          },
          { id: 456 },
        ];
        expectedResult = service.addConnectionToCollectionIfMissing(connectionCollection, connection);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Connection to an array that doesn't contain it", () => {
        const connection: IConnection = { id: 123 };
        const connectionCollection: IConnection[] = [{ id: 456 }];
        expectedResult = service.addConnectionToCollectionIfMissing(connectionCollection, connection);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(connection);
      });

      it('should add only unique Connection to an array', () => {
        const connectionArray: IConnection[] = [{ id: 123 }, { id: 456 }, { id: 52487 }];
        const connectionCollection: IConnection[] = [{ id: 123 }];
        expectedResult = service.addConnectionToCollectionIfMissing(connectionCollection, ...connectionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const connection: IConnection = { id: 123 };
        const connection2: IConnection = { id: 456 };
        expectedResult = service.addConnectionToCollectionIfMissing([], connection, connection2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(connection);
        expect(expectedResult).toContain(connection2);
      });

      it('should accept null and undefined values', () => {
        const connection: IConnection = { id: 123 };
        expectedResult = service.addConnectionToCollectionIfMissing([], null, connection, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(connection);
      });

      it('should return initial array if no Connection is added', () => {
        const connectionCollection: IConnection[] = [{ id: 123 }];
        expectedResult = service.addConnectionToCollectionIfMissing(connectionCollection, undefined, null);
        expect(expectedResult).toEqual(connectionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
