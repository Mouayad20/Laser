import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IShipmentType, ShipmentType } from '../shipment-type.model';

import { ShipmentTypeService } from './shipment-type.service';

describe('ShipmentType Service', () => {
  let service: ShipmentTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IShipmentType;
  let expectedResult: IShipmentType | IShipmentType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ShipmentTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      factor: 0,
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

    it('should create a ShipmentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ShipmentType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShipmentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          factor: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShipmentType', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new ShipmentType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShipmentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          factor: 1,
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

    it('should delete a ShipmentType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addShipmentTypeToCollectionIfMissing', () => {
      it('should add a ShipmentType to an empty array', () => {
        const shipmentType: IShipmentType = { id: 123 };
        expectedResult = service.addShipmentTypeToCollectionIfMissing([], shipmentType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shipmentType);
      });

      it('should not add a ShipmentType to an array that contains it', () => {
        const shipmentType: IShipmentType = { id: 123 };
        const shipmentTypeCollection: IShipmentType[] = [
          {
            ...shipmentType,
          },
          { id: 456 },
        ];
        expectedResult = service.addShipmentTypeToCollectionIfMissing(shipmentTypeCollection, shipmentType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShipmentType to an array that doesn't contain it", () => {
        const shipmentType: IShipmentType = { id: 123 };
        const shipmentTypeCollection: IShipmentType[] = [{ id: 456 }];
        expectedResult = service.addShipmentTypeToCollectionIfMissing(shipmentTypeCollection, shipmentType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shipmentType);
      });

      it('should add only unique ShipmentType to an array', () => {
        const shipmentTypeArray: IShipmentType[] = [{ id: 123 }, { id: 456 }, { id: 39320 }];
        const shipmentTypeCollection: IShipmentType[] = [{ id: 123 }];
        expectedResult = service.addShipmentTypeToCollectionIfMissing(shipmentTypeCollection, ...shipmentTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shipmentType: IShipmentType = { id: 123 };
        const shipmentType2: IShipmentType = { id: 456 };
        expectedResult = service.addShipmentTypeToCollectionIfMissing([], shipmentType, shipmentType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shipmentType);
        expect(expectedResult).toContain(shipmentType2);
      });

      it('should accept null and undefined values', () => {
        const shipmentType: IShipmentType = { id: 123 };
        expectedResult = service.addShipmentTypeToCollectionIfMissing([], null, shipmentType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shipmentType);
      });

      it('should return initial array if no ShipmentType is added', () => {
        const shipmentTypeCollection: IShipmentType[] = [{ id: 123 }];
        expectedResult = service.addShipmentTypeToCollectionIfMissing(shipmentTypeCollection, undefined, null);
        expect(expectedResult).toEqual(shipmentTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
