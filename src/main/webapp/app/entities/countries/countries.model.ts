export interface ICountries {
  id?: number;
  country?: string | null;
  capital?: string | null;
  code?: string | null;
  phoneCode?: string | null;
}

export class Countries implements ICountries {
  constructor(
    public id?: number,
    public country?: string | null,
    public capital?: string | null,
    public code?: string | null,
    public phoneCode?: string | null
  ) {}
}

export function getCountriesIdentifier(countries: ICountries): number | undefined {
  return countries.id;
}
