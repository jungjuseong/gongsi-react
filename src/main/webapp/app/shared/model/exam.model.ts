import { IQuiz } from 'app/shared/model/quiz.model';
import { IAgency } from 'app/shared/model/agency.model';
import { ILicense } from 'app/shared/model/license.model';
import { ExamType } from 'app/shared/model/enumerations/exam-type.model';

export interface IExam {
  id?: number;
  title?: string;
  examType?: keyof typeof ExamType | null;
  effectiveDate?: string;
  quizzes?: IQuiz[] | null;
  implementingAgency?: IAgency | null;
  license?: ILicense | null;
}

export const defaultValue: Readonly<IExam> = {};
