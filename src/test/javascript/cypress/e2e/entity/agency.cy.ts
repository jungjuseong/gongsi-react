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

describe('Agency e2e test', () => {
  const agencyPageUrl = '/agency';
  const agencyPageUrlPattern = new RegExp('/agency(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const agencySample = { name: 'untried medium' };

  let agency;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/agencies+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/agencies').as('postEntityRequest');
    cy.intercept('DELETE', '/api/agencies/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (agency) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/agencies/${agency.id}`,
      }).then(() => {
        agency = undefined;
      });
    }
  });

  it('Agencies menu should load Agencies page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('agency');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Agency').should('exist');
    cy.url().should('match', agencyPageUrlPattern);
  });

  describe('Agency page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(agencyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Agency page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/agency/new$'));
        cy.getEntityCreateUpdateHeading('Agency');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/agencies',
          body: agencySample,
        }).then(({ body }) => {
          agency = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/agencies+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [agency],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(agencyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Agency page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('agency');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyPageUrlPattern);
      });

      it('edit button click should load edit Agency page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Agency');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyPageUrlPattern);
      });

      it('edit button click should load edit Agency page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Agency');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyPageUrlPattern);
      });

      it('last delete button click should delete instance of Agency', () => {
        cy.intercept('GET', '/api/agencies/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('agency').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyPageUrlPattern);

        agency = undefined;
      });
    });
  });

  describe('new Agency page', () => {
    beforeEach(() => {
      cy.visit(`${agencyPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Agency');
    });

    it('should create an instance of Agency', () => {
      cy.get(`[data-cy="name"]`).type('problem');
      cy.get(`[data-cy="name"]`).should('have.value', 'problem');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        agency = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', agencyPageUrlPattern);
    });
  });
});
