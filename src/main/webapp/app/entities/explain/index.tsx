import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Explain from './explain';
import ExplainDetail from './explain-detail';
import ExplainUpdate from './explain-update';
import ExplainDeleteDialog from './explain-delete-dialog';

const ExplainRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Explain />} />
    <Route path="new" element={<ExplainUpdate />} />
    <Route path=":id">
      <Route index element={<ExplainDetail />} />
      <Route path="edit" element={<ExplainUpdate />} />
      <Route path="delete" element={<ExplainDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ExplainRoutes;
