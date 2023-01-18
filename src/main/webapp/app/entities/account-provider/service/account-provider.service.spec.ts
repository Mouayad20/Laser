import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAccountProvider, AccountProvider } from '../account-provider.model';

import { AccountProviderService } from './account-provider.service';

describe('AccountProvider Service', () => {
  let service: AccountProviderService;
  let httpMock: HttpTestingController;
  let elemDefault: IAccountProvider;
  let expectedResult: IAccountProvider | IAccountProvider[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccountProviderService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a AccountProvider', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AccountProvider()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccountProvider', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccountProvider', () => {
      const patchObject = Object.assign({}, new AccountProvider());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccountProvider', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a AccountProvider', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAccountProviderToCollectionIfMissing', () => {
      it('should add a AccountProvider to an empty array', () => {
        const accountProvider: IAccountProvider = { id: 123 };
        expectedResult = service.addAccountProviderToCollectionIfMissing([], accountProvider);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountProvider);
      });

      it('should not add a AccountProvider to an array that contains it', () => {
        const accountProvider: IAccountProvider = { id: 123 };
        const accountProviderCollection: IAccountProvider[] = [
          {
            ...accountProvider,
          },
          { id: 456 },
        ];
        expectedResult = service.addAccountProviderToCollectionIfMissing(accountProviderCollection, accountProvider);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccountProvider to an array that doesn't contain it", () => {
        const accountProvider: IAccountProvider = { id: 123 };
        const accountProviderCollection: IAccountProvider[] = [{ id: 456 }];
        expectedResult = service.addAccountProviderToCollectionIfMissing(accountProviderCollection, accountProvider);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountProvider);
      });

      it('should add only unique AccountProvider to an array', () => {
        const accountProviderArray: IAccountProvider[] = [{ id: 123 }, { id: 456 }, { id: 36042 }];
        const accountProviderCollection: IAccountProvider[] = [{ id: 123 }];
        expectedResult = service.addAccountProviderToCollectionIfMissing(accountProviderCollection, ...accountProviderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accountProvider: IAccountProvider = { id: 123 };
        const accountProvider2: IAccountProvider = { id: 456 };
        expectedResult = service.addAccountProviderToCollectionIfMissing([], accountProvider, accountProvider2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountProvider);
        expect(expectedResult).toContain(accountProvider2);
      });

      it('should accept null and undefined values', () => {
        const accountProvider: IAccountProvider = { id: 123 };
        expectedResult = service.addAccountProviderToCollectionIfMissing([], null, accountProvider, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountProvider);
      });

      it('should return initial array if no AccountProvider is added', () => {
        const accountProviderCollection: IAccountProvider[] = [{ id: 123 }];
        expectedResult = service.addAccountProviderToCollectionIfMissing(accountProviderCollection, undefined, null);
        expect(expectedResult).toEqual(accountProviderCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
