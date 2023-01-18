import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICountries, Countries } from '../countries.model';

import { CountriesService } from './countries.service';

describe('Countries Service', () => {
  let service: CountriesService;
  let httpMock: HttpTestingController;
  let elemDefault: ICountries;
  let expectedResult: ICountries | ICountries[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CountriesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      country: 'AAAAAAA',
      capital: 'AAAAAAA',
      code: 'AAAAAAA',
      phoneCode: 'AAAAAAA',
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

    it('should create a Countries', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Countries()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Countries', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          country: 'BBBBBB',
          capital: 'BBBBBB',
          code: 'BBBBBB',
          phoneCode: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Countries', () => {
      const patchObject = Object.assign(
        {
          code: 'BBBBBB',
          phoneCode: 'BBBBBB',
        },
        new Countries()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Countries', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          country: 'BBBBBB',
          capital: 'BBBBBB',
          code: 'BBBBBB',
          phoneCode: 'BBBBBB',
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

    it('should delete a Countries', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCountriesToCollectionIfMissing', () => {
      it('should add a Countries to an empty array', () => {
        const countries: ICountries = { id: 123 };
        expectedResult = service.addCountriesToCollectionIfMissing([], countries);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countries);
      });

      it('should not add a Countries to an array that contains it', () => {
        const countries: ICountries = { id: 123 };
        const countriesCollection: ICountries[] = [
          {
            ...countries,
          },
          { id: 456 },
        ];
        expectedResult = service.addCountriesToCollectionIfMissing(countriesCollection, countries);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Countries to an array that doesn't contain it", () => {
        const countries: ICountries = { id: 123 };
        const countriesCollection: ICountries[] = [{ id: 456 }];
        expectedResult = service.addCountriesToCollectionIfMissing(countriesCollection, countries);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countries);
      });

      it('should add only unique Countries to an array', () => {
        const countriesArray: ICountries[] = [{ id: 123 }, { id: 456 }, { id: 79382 }];
        const countriesCollection: ICountries[] = [{ id: 123 }];
        expectedResult = service.addCountriesToCollectionIfMissing(countriesCollection, ...countriesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const countries: ICountries = { id: 123 };
        const countries2: ICountries = { id: 456 };
        expectedResult = service.addCountriesToCollectionIfMissing([], countries, countries2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countries);
        expect(expectedResult).toContain(countries2);
      });

      it('should accept null and undefined values', () => {
        const countries: ICountries = { id: 123 };
        expectedResult = service.addCountriesToCollectionIfMissing([], null, countries, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countries);
      });

      it('should return initial array if no Countries is added', () => {
        const countriesCollection: ICountries[] = [{ id: 123 }];
        expectedResult = service.addCountriesToCollectionIfMissing(countriesCollection, undefined, null);
        expect(expectedResult).toEqual(countriesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
