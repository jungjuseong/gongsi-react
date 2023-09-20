import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('License e2e test', () => {
  const licensePageUrl = '/license';
  const licensePageUrlPattern = new RegExp('/license(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const licenseSample = { title: 'trapdoor gifted phew' };

  let license;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/licenses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/licenses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/licenses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (license) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/licenses/${license.id}`,
      }).then(() => {
        license = undefined;
      });
    }
  });

  it('Licenses menu should load Licenses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('license');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('License').should('exist');
    cy.url().should('match', licensePageUrlPattern);
  });

  describe('License page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(licensePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create License page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/license/new$'));
        cy.getEntityCreateUpdateHeading('License');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', licensePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/licenses',
          body: licenseSample,
        }).then(({ body }) => {
          license = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/licenses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [license],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(licensePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details License page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('license');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', licensePageUrlPattern);
      });

      it('edit button click should load edit License page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('License');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', licensePageUrlPattern);
      });

      it('edit button click should load edit License page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('License');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', licensePageUrlPattern);
      });

      it('last delete button click should delete instance of License', () => {
        cy.intercept('GET', '/api/licenses/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('license').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', licensePageUrlPattern);

        license = undefined;
      });
    });
  });

  describe('new License page', () => {
    beforeEach(() => {
      cy.visit(`${licensePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('License');
    });

    it('should create an instance of License', () => {
      cy.get(`[data-cy="title"]`).type('entire gee');
      cy.get(`[data-cy="title"]`).should('have.value', 'entire gee');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        license = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', licensePageUrlPattern);
    });
  });
});
