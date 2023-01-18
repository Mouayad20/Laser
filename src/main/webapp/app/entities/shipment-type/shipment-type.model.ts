import { IShipment } from 'app/entities/shipment/shipment.model';

export interface IShipmentType {
  id?: number;
  name?: string | null;
  factor?: number | null;
  shipments?: IShipment[] | null;
}

export class ShipmentType implements IShipmentType {
  constructor(public id?: number, public name?: string | null, public factor?: number | null, public shipments?: IShipment[] | null) {}
}

export function getShipmentTypeIdentifier(shipmentType: IShipmentType): number | undefined {
  return shipmentType.id;
}
