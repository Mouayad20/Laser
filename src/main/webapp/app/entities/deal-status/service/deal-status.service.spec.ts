import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDealStatus, DealStatus } from '../deal-status.model';

import { DealStatusService } from './deal-status.service';

describe('DealStatus Service', () => {
  let service: DealStatusService;
  let httpMock: HttpTestingController;
  let elemDefault: IDealStatus;
  let expectedResult: IDealStatus | IDealStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DealStatusService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      sequence: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DealStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DealStatus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DealStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          sequence: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DealStatus', () => {
      const patchObject = Object.assign({}, new DealStatus());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DealStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          sequence: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DealStatus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDealStatusToCollectionIfMissing', () => {
      it('should add a DealStatus to an empty array', () => {
        const dealStatus: IDealStatus = { id: 123 };
        expectedResult = service.addDealStatusToCollectionIfMissing([], dealStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dealStatus);
      });

      it('should not add a DealStatus to an array that contains it', () => {
        const dealStatus: IDealStatus = { id: 123 };
        const dealStatusCollection: IDealStatus[] = [
          {
            ...dealStatus,
          },
          { id: 456 },
        ];
        expectedResult = service.addDealStatusToCollectionIfMissing(dealStatusCollection, dealStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DealStatus to an array that doesn't contain it", () => {
        const dealStatus: IDealStatus = { id: 123 };
        const dealStatusCollection: IDealStatus[] = [{ id: 456 }];
        expectedResult = service.addDealStatusToCollectionIfMissing(dealStatusCollection, dealStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dealStatus);
      });

      it('should add only unique DealStatus to an array', () => {
        const dealStatusArray: IDealStatus[] = [{ id: 123 }, { id: 456 }, { id: 9877 }];
        const dealStatusCollection: IDealStatus[] = [{ id: 123 }];
        expectedResult = service.addDealStatusToCollectionIfMissing(dealStatusCollection, ...dealStatusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dealStatus: IDealStatus = { id: 123 };
        const dealStatus2: IDealStatus = { id: 456 };
        expectedResult = service.addDealStatusToCollectionIfMissing([], dealStatus, dealStatus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dealStatus);
        expect(expectedResult).toContain(dealStatus2);
      });

      it('should accept null and undefined values', () => {
        const dealStatus: IDealStatus = { id: 123 };
        expectedResult = service.addDealStatusToCollectionIfMissing([], null, dealStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dealStatus);
      });

      it('should return initial array if no DealStatus is added', () => {
        const dealStatusCollection: IDealStatus[] = [{ id: 123 }];
        expectedResult = service.addDealStatusToCollectionIfMissing(dealStatusCollection, undefined, null);
        expect(expectedResult).toEqual(dealStatusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
