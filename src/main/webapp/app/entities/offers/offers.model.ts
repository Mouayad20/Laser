export interface IOffers {
  id?: number;
  shipmentDealId?: number | null;
  tripDealId?: number | null;
  status?: string | null;
  senderId?: number | null;
}

export class Offers implements IOffers {
  constructor(
    public id?: number,
    public shipmentDealId?: number | null,
    public tripDealId?: number | null,
    public status?: string | null,
    public senderId?: number | null
  ) {}
}

export function getOffersIdentifier(offers: IOffers): number | undefined {
  return offers.id;
}
