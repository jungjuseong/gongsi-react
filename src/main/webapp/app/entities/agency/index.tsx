import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Agency from './agency';
import AgencyDetail from './agency-detail';
import AgencyUpdate from './agency-update';
import AgencyDeleteDialog from './agency-delete-dialog';

const AgencyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Agency />} />
    <Route path="new" element={<AgencyUpdate />} />
    <Route path=":id">
      <Route index element={<AgencyDetail />} />
      <Route path="edit" element={<AgencyUpdate />} />
      <Route path="delete" element={<AgencyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AgencyRoutes;
