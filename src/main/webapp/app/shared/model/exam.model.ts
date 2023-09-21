import { IQuiz } from 'app/shared/model/quiz.model';
import { IAgency } from 'app/shared/model/agency.model';
import { ILicense } from 'app/shared/model/license.model';
import { SubjectType } from 'app/shared/model/enumerations/subject-type.model';

export interface IExam {
  id?: number;
  title?: string;
  subject?: keyof typeof SubjectType | null;
  date?: string;
  quizzes?: IQuiz[] | null;
  agency?: IAgency | null;
  license?: ILicense | null;
}

export const defaultValue: Readonly<IExam> = {};
