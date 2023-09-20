import { IExam } from 'app/shared/model/exam.model';

export interface ILicense {
  id?: number;
  title?: string;
  exams?: IExam[] | null;
}

export const defaultValue: Readonly<ILicense> = {};
