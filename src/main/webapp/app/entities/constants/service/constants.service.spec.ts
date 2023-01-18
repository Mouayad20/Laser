import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConstants, Constants } from '../constants.model';

import { ConstantsService } from './constants.service';

describe('Constants Service', () => {
  let service: ConstantsService;
  let httpMock: HttpTestingController;
  let elemDefault: IConstants;
  let expectedResult: IConstants | IConstants[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConstantsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      weightFactor: 0,
      maxWeight: 0,
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

    it('should create a Constants', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Constants()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Constants', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          weightFactor: 1,
          maxWeight: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Constants', () => {
      const patchObject = Object.assign(
        {
          weightFactor: 1,
        },
        new Constants()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Constants', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          weightFactor: 1,
          maxWeight: 1,
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

    it('should delete a Constants', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addConstantsToCollectionIfMissing', () => {
      it('should add a Constants to an empty array', () => {
        const constants: IConstants = { id: 123 };
        expectedResult = service.addConstantsToCollectionIfMissing([], constants);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(constants);
      });

      it('should not add a Constants to an array that contains it', () => {
        const constants: IConstants = { id: 123 };
        const constantsCollection: IConstants[] = [
          {
            ...constants,
          },
          { id: 456 },
        ];
        expectedResult = service.addConstantsToCollectionIfMissing(constantsCollection, constants);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Constants to an array that doesn't contain it", () => {
        const constants: IConstants = { id: 123 };
        const constantsCollection: IConstants[] = [{ id: 456 }];
        expectedResult = service.addConstantsToCollectionIfMissing(constantsCollection, constants);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(constants);
      });

      it('should add only unique Constants to an array', () => {
        const constantsArray: IConstants[] = [{ id: 123 }, { id: 456 }, { id: 81056 }];
        const constantsCollection: IConstants[] = [{ id: 123 }];
        expectedResult = service.addConstantsToCollectionIfMissing(constantsCollection, ...constantsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const constants: IConstants = { id: 123 };
        const constants2: IConstants = { id: 456 };
        expectedResult = service.addConstantsToCollectionIfMissing([], constants, constants2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(constants);
        expect(expectedResult).toContain(constants2);
      });

      it('should accept null and undefined values', () => {
        const constants: IConstants = { id: 123 };
        expectedResult = service.addConstantsToCollectionIfMissing([], null, constants, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(constants);
      });

      it('should return initial array if no Constants is added', () => {
        const constantsCollection: IConstants[] = [{ id: 123 }];
        expectedResult = service.addConstantsToCollectionIfMissing(constantsCollection, undefined, null);
        expect(expectedResult).toEqual(constantsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
