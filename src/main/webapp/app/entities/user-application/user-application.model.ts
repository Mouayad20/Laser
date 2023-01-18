import dayjs from 'dayjs/esm';
import { IConnection } from 'app/entities/connection/connection.model';
import { IDeal } from 'app/entities/deal/deal.model';
import { IUser } from 'app/entities/user/user.model';

export interface IUserApplication {
  id?: number;
  phone?: string | null;
  passport?: string | null;
  createdAt?: dayjs.Dayjs | null;
  isGoogleAccount?: boolean | null;
  isFacebookAccount?: boolean | null;
  isTwitterAccount?: boolean | null;
  image?: string | null;
  rate?: number | null;
  fiveStar?: number | null;
  fourSatr?: number | null;
  threeStar?: number | null;
  twoStar?: number | null;
  oneStar?: number | null;
  detalis?: string | null;
  connection?: IConnection | null;
  tripsDeals?: IDeal[] | null;
  shipmentDeals?: IDeal[] | null;
  user?: IUser | null;
}

export class UserApplication implements IUserApplication {
  constructor(
    public id?: number,
    public phone?: string | null,
    public passport?: string | null,
    public createdAt?: dayjs.Dayjs | null,
    public isGoogleAccount?: boolean | null,
    public isFacebookAccount?: boolean | null,
    public isTwitterAccount?: boolean | null,
    public image?: string | null,
    public rate?: number | null,
    public fiveStar?: number | null,
    public fourSatr?: number | null,
    public threeStar?: number | null,
    public twoStar?: number | null,
    public oneStar?: number | null,
    public detalis?: string | null,
    public connection?: IConnection | null,
    public tripsDeals?: IDeal[] | null,
    public shipmentDeals?: IDeal[] | null,
    public user?: IUser | null
  ) {
    this.isGoogleAccount = this.isGoogleAccount ?? false;
    this.isFacebookAccount = this.isFacebookAccount ?? false;
    this.isTwitterAccount = this.isTwitterAccount ?? false;
  }
}

export function getUserApplicationIdentifier(userApplication: IUserApplication): number | undefined {
  return userApplication.id;
}
