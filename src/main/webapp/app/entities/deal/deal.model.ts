import dayjs from 'dayjs/esm';
import { ITransaction } from 'app/entities/transaction/transaction.model';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { IUserApplication } from 'app/entities/user-application/user-application.model';
import { ITrip } from 'app/entities/trip/trip.model';
import { IDealStatus } from 'app/entities/deal-status/deal-status.model';

export interface IDeal {
  id?: number;
  totalPrice?: number | null;
  isCashed?: boolean | null;
  fromAccount?: string | null;
  toAccount?: string | null;
  fullWeight?: number | null;
  availableWeight?: number | null;
  arrivelDate?: dayjs.Dayjs | null;
  expectedDate?: dayjs.Dayjs | null;
  details?: string | null;
  transaction?: ITransaction | null;
  shipments?: IShipment[] | null;
  deliver?: IUserApplication | null;
  owner?: IUserApplication | null;
  trip?: ITrip | null;
  status?: IDealStatus | null;
}

export class Deal implements IDeal {
  constructor(
    public id?: number,
    public totalPrice?: number | null,
    public isCashed?: boolean | null,
    public fromAccount?: string | null,
    public toAccount?: string | null,
    public fullWeight?: number | null,
    public availableWeight?: number | null,
    public arrivelDate?: dayjs.Dayjs | null,
    public expectedDate?: dayjs.Dayjs | null,
    public details?: string | null,
    public transaction?: ITransaction | null,
    public shipments?: IShipment[] | null,
    public deliver?: IUserApplication | null,
    public owner?: IUserApplication | null,
    public trip?: ITrip | null,
    public status?: IDealStatus | null
  ) {
    this.isCashed = this.isCashed ?? false;
  }
}

export function getDealIdentifier(deal: IDeal): number | undefined {
  return deal.id;
}
