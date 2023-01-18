import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IAccountProvider, AccountProvider } from '../account-provider.model';
import { AccountProviderService } from '../service/account-provider.service';

import { AccountProviderRoutingResolveService } from './account-provider-routing-resolve.service';

describe('AccountProvider routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AccountProviderRoutingResolveService;
  let service: AccountProviderService;
  let resultAccountProvider: IAccountProvider | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(AccountProviderRoutingResolveService);
    service = TestBed.inject(AccountProviderService);
    resultAccountProvider = undefined;
  });

  describe('resolve', () => {
    it('should return IAccountProvider returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountProvider = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAccountProvider).toEqual({ id: 123 });
    });

    it('should return new IAccountProvider if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountProvider = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAccountProvider).toEqual(new AccountProvider());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AccountProvider })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountProvider = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAccountProvider).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
