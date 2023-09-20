import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Quiz from './quiz';
import QuizDetail from './quiz-detail';
import QuizUpdate from './quiz-update';
import QuizDeleteDialog from './quiz-delete-dialog';

const QuizRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Quiz />} />
    <Route path="new" element={<QuizUpdate />} />
    <Route path=":id">
      <Route index element={<QuizDetail />} />
      <Route path="edit" element={<QuizUpdate />} />
      <Route path="delete" element={<QuizDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QuizRoutes;
