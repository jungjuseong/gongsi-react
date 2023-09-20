import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/exam">
        <Translate contentKey="global.menu.entities.exam" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/agency">
        <Translate contentKey="global.menu.entities.agency" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/quiz">
        <Translate contentKey="global.menu.entities.quiz" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/explain">
        <Translate contentKey="global.menu.entities.explain" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/license">
        <Translate contentKey="global.menu.entities.license" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
