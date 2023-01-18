import { IDeal } from 'app/entities/deal/deal.model';
import { IAccountProvider } from 'app/entities/account-provider/account-provider.model';

export interface ITransaction {
  id?: number;
  fromAccount?: string | null;
  toAccount?: string | null;
  fees?: number | null;
  netAmount?: number | null;
  details?: string | null;
  deal?: IDeal | null;
  provider?: IAccountProvider | null;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public fromAccount?: string | null,
    public toAccount?: string | null,
    public fees?: number | null,
    public netAmount?: number | null,
    public details?: string | null,
    public deal?: IDeal | null,
    public provider?: IAccountProvider | null
  ) {}
}

export function getTransactionIdentifier(transaction: ITransaction): number | undefined {
  return transaction.id;
}
