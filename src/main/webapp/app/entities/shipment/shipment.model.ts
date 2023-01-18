import dayjs from 'dayjs/esm';
import { IShipmentType } from 'app/entities/shipment-type/shipment-type.model';
import { ILocation } from 'app/entities/location/location.model';
import { IDeal } from 'app/entities/deal/deal.model';

export interface IShipment {
  id?: number;
  weight?: number | null;
  url?: string | null;
  description?: string | null;
  createdAt?: dayjs.Dayjs | null;
  imgUrl?: string | null;
  cost?: number | null;
  price?: number | null;
  details?: string | null;
  type?: IShipmentType | null;
  to?: ILocation | null;
  from?: ILocation | null;
  deal?: IDeal | null;
}

export class Shipment implements IShipment {
  constructor(
    public id?: number,
    public weight?: number | null,
    public url?: string | null,
    public description?: string | null,
    public createdAt?: dayjs.Dayjs | null,
    public imgUrl?: string | null,
    public cost?: number | null,
    public price?: number | null,
    public details?: string | null,
    public type?: IShipmentType | null,
    public to?: ILocation | null,
    public from?: ILocation | null,
    public deal?: IDeal | null
  ) {}
}

export function getShipmentIdentifier(shipment: IShipment): number | undefined {
  return shipment.id;
}
