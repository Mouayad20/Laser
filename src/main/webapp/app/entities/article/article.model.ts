import dayjs from 'dayjs/esm';

export interface IArticle {
  id?: number;
  title?: string | null;
  content?: string | null;
  viewcount?: number | null;
  imageUrl?: string | null;
  createdAt?: dayjs.Dayjs | null;
  detalis?: string | null;
}

export class Article implements IArticle {
  constructor(
    public id?: number,
    public title?: string | null,
    public content?: string | null,
    public viewcount?: number | null,
    public imageUrl?: string | null,
    public createdAt?: dayjs.Dayjs | null,
    public detalis?: string | null
  ) {}
}

export function getArticleIdentifier(article: IArticle): number | undefined {
  return article.id;
}
