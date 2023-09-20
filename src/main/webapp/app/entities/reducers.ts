import exam from 'app/entities/exam/exam.reducer';
import agency from 'app/entities/agency/agency.reducer';
import quiz from 'app/entities/quiz/quiz.reducer';
import explain from 'app/entities/explain/explain.reducer';
import license from 'app/entities/license/license.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  exam,
  agency,
  quiz,
  explain,
  license,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
