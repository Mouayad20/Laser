import { IDeal } from 'app/entities/deal/deal.model';

export interface IDealStatus {
  id?: number;
  name?: string | null;
  sequence?: number | null;
  deals?: IDeal[] | null;
}

export class DealStatus implements IDealStatus {
  constructor(public id?: number, public name?: string | null, public sequence?: number | null, public deals?: IDeal[] | null) {}
}

export function getDealStatusIdentifier(dealStatus: IDealStatus): number | undefined {
  return dealStatus.id;
}
