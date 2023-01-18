import dayjs from 'dayjs/esm';
import { IUserApplication } from 'app/entities/user-application/user-application.model';

export interface IConnection {
  id?: number;
  fcmToken?: string | null;
  localToken?: string | null;
  localRefreshToken?: string | null;
  oAuthToken?: string | null;
  localTokenExpiryDate?: dayjs.Dayjs | null;
  userApplication?: IUserApplication | null;
}

export class Connection implements IConnection {
  constructor(
    public id?: number,
    public fcmToken?: string | null,
    public localToken?: string | null,
    public localRefreshToken?: string | null,
    public oAuthToken?: string | null,
    public localTokenExpiryDate?: dayjs.Dayjs | null,
    public userApplication?: IUserApplication | null
  ) {}
}

export function getConnectionIdentifier(connection: IConnection): number | undefined {
  return connection.id;
}
