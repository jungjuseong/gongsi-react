import { IQuiz } from 'app/shared/model/quiz.model';
import { AnswerType } from 'app/shared/model/enumerations/answer-type.model';

export interface IExplain {
  id?: number;
  answer?: keyof typeof AnswerType;
  description?: string | null;
  quiz?: IQuiz | null;
}

export const defaultValue: Readonly<IExplain> = {};
