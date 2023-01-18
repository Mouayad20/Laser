import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOffers, Offers } from '../offers.model';

import { OffersService } from './offers.service';

describe('Offers Service', () => {
  let service: OffersService;
  let httpMock: HttpTestingController;
  let elemDefault: IOffers;
  let expectedResult: IOffers | IOffers[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OffersService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      shipmentDealId: 0,
      tripDealId: 0,
      status: 'AAAAAAA',
      senderId: 0,
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

    it('should create a Offers', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Offers()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Offers', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          shipmentDealId: 1,
          tripDealId: 1,
          status: 'BBBBBB',
          senderId: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Offers', () => {
      const patchObject = Object.assign(
        {
          shipmentDealId: 1,
          tripDealId: 1,
          status: 'BBBBBB',
          senderId: 1,
        },
        new Offers()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Offers', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          shipmentDealId: 1,
          tripDealId: 1,
          status: 'BBBBBB',
          senderId: 1,
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

    it('should delete a Offers', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOffersToCollectionIfMissing', () => {
      it('should add a Offers to an empty array', () => {
        const offers: IOffers = { id: 123 };
        expectedResult = service.addOffersToCollectionIfMissing([], offers);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(offers);
      });

      it('should not add a Offers to an array that contains it', () => {
        const offers: IOffers = { id: 123 };
        const offersCollection: IOffers[] = [
          {
            ...offers,
          },
          { id: 456 },
        ];
        expectedResult = service.addOffersToCollectionIfMissing(offersCollection, offers);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Offers to an array that doesn't contain it", () => {
        const offers: IOffers = { id: 123 };
        const offersCollection: IOffers[] = [{ id: 456 }];
        expectedResult = service.addOffersToCollectionIfMissing(offersCollection, offers);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(offers);
      });

      it('should add only unique Offers to an array', () => {
        const offersArray: IOffers[] = [{ id: 123 }, { id: 456 }, { id: 8839 }];
        const offersCollection: IOffers[] = [{ id: 123 }];
        expectedResult = service.addOffersToCollectionIfMissing(offersCollection, ...offersArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const offers: IOffers = { id: 123 };
        const offers2: IOffers = { id: 456 };
        expectedResult = service.addOffersToCollectionIfMissing([], offers, offers2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(offers);
        expect(expectedResult).toContain(offers2);
      });

      it('should accept null and undefined values', () => {
        const offers: IOffers = { id: 123 };
        expectedResult = service.addOffersToCollectionIfMissing([], null, offers, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(offers);
      });

      it('should return initial array if no Offers is added', () => {
        const offersCollection: IOffers[] = [{ id: 123 }];
        expectedResult = service.addOffersToCollectionIfMissing(offersCollection, undefined, null);
        expect(expectedResult).toEqual(offersCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
