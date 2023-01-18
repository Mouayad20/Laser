export interface IConstants {
  id?: number;
  weightFactor?: number | null;
  maxWeight?: number | null;
}

export class Constants implements IConstants {
  constructor(public id?: number, public weightFactor?: number | null, public maxWeight?: number | null) {}
}

export function getConstantsIdentifier(constants: IConstants): number | undefined {
  return constants.id;
}
