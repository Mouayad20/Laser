import { ITransaction } from 'app/entities/transaction/transaction.model';

export interface IAccountProvider {
  id?: number;
  name?: string | null;
  transactions?: ITransaction[] | null;
}

export class AccountProvider implements IAccountProvider {
  constructor(public id?: number, public name?: string | null, public transactions?: ITransaction[] | null) {}
}

export function getAccountProviderIdentifier(accountProvider: IAccountProvider): number | undefined {
  return accountProvider.id;
}
