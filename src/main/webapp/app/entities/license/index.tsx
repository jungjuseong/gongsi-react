import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import License from './license';
import LicenseDetail from './license-detail';
import LicenseUpdate from './license-update';
import LicenseDeleteDialog from './license-delete-dialog';

const LicenseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<License />} />
    <Route path="new" element={<LicenseUpdate />} />
    <Route path=":id">
      <Route index element={<LicenseDetail />} />
      <Route path="edit" element={<LicenseUpdate />} />
      <Route path="delete" element={<LicenseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LicenseRoutes;
