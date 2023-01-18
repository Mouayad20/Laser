import dayjs from 'dayjs/esm';
import { ITrip } from 'app/entities/trip/trip.model';
import { IShipment } from 'app/entities/shipment/shipment.model';

export interface ILocation {
  id?: number;
  country?: string | null;
  city?: string | null;
  airport?: string | null;
  createdAt?: dayjs.Dayjs | null;
  details?: string | null;
  tripDestinations?: ITrip[] | null;
  tripSources?: ITrip[] | null;
  shipmentDestinations?: IShipment[] | null;
  shipmentSources?: IShipment[] | null;
}

export class Location implements ILocation {
  constructor(
    public id?: number,
    public country?: string | null,
    public city?: string | null,
    public airport?: string | null,
    public createdAt?: dayjs.Dayjs | null,
    public details?: string | null,
    public tripDestinations?: ITrip[] | null,
    public tripSources?: ITrip[] | null,
    public shipmentDestinations?: IShipment[] | null,
    public shipmentSources?: IShipment[] | null
  ) {}
}

export function getLocationIdentifier(location: ILocation): number | undefined {
  return location.id;
}
