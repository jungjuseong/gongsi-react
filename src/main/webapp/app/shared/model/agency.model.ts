import { IExam } from 'app/shared/model/exam.model';

export interface IAgency {
  id?: number;
  name?: string;
  exams?: IExam[] | null;
}

export const defaultValue: Readonly<IAgency> = {};
