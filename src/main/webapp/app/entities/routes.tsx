import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Exam from './exam';
import Agency from './agency';
import Quiz from './quiz';
import Explain from './explain';
import License from './license';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="exam/*" element={<Exam />} />
        <Route path="agency/*" element={<Agency />} />
        <Route path="quiz/*" element={<Quiz />} />
        <Route path="explain/*" element={<Explain />} />
        <Route path="license/*" element={<License />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
