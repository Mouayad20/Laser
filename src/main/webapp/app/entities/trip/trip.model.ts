import dayjs from 'dayjs/esm';
import { IDeal } from 'app/entities/deal/deal.model';
import { ILocation } from 'app/entities/location/location.model';

export interface ITrip {
  id?: number;
  createdAt?: dayjs.Dayjs | null;
  flyTime?: dayjs.Dayjs | null;
  arriveTime?: dayjs.Dayjs | null;
  tripIdentifier?: string | null;
  details?: string | null;
  ticketImage?: string | null;
  tripType?: string | null;
  transit?: string | null;
  deals?: IDeal[] | null;
  to?: ILocation | null;
  from?: ILocation | null;
}

export class Trip implements ITrip {
  constructor(
    public id?: number,
    public createdAt?: dayjs.Dayjs | null,
    public flyTime?: dayjs.Dayjs | null,
    public arriveTime?: dayjs.Dayjs | null,
    public tripIdentifier?: string | null,
    public details?: string | null,
    public ticketImage?: string | null,
    public tripType?: string | null,
    public transit?: string | null,
    public deals?: IDeal[] | null,
    public to?: ILocation | null,
    public from?: ILocation | null
  ) {}
}

export function getTripIdentifier(trip: ITrip): number | undefined {
  return trip.id;
}
