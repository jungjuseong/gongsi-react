import { IExplain } from 'app/shared/model/explain.model';
import { IExam } from 'app/shared/model/exam.model';

export interface IQuiz {
  id?: number;
  code?: string;
  question?: string;
  example?: string | null;
  selections?: string | null;
  explains?: IExplain[] | null;
  exam?: IExam | null;
}

export const defaultValue: Readonly<IQuiz> = {};
