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

describe('Explain e2e test', () => {
  const explainPageUrl = '/explain';
  const explainPageUrlPattern = new RegExp('/explain(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const explainSample = { answer: 'Q1' };

  let explain;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/explains+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/explains').as('postEntityRequest');
    cy.intercept('DELETE', '/api/explains/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (explain) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/explains/${explain.id}`,
      }).then(() => {
        explain = undefined;
      });
    }
  });

  it('Explains menu should load Explains page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('explain');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Explain').should('exist');
    cy.url().should('match', explainPageUrlPattern);
  });

  describe('Explain page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(explainPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Explain page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/explain/new$'));
        cy.getEntityCreateUpdateHeading('Explain');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', explainPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/explains',
          body: explainSample,
        }).then(({ body }) => {
          explain = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/explains+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [explain],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(explainPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Explain page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('explain');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', explainPageUrlPattern);
      });

      it('edit button click should load edit Explain page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Explain');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', explainPageUrlPattern);
      });

      it('edit button click should load edit Explain page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Explain');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', explainPageUrlPattern);
      });

      it('last delete button click should delete instance of Explain', () => {
        cy.intercept('GET', '/api/explains/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('explain').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', explainPageUrlPattern);

        explain = undefined;
      });
    });
  });

  describe('new Explain page', () => {
    beforeEach(() => {
      cy.visit(`${explainPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Explain');
    });

    it('should create an instance of Explain', () => {
      cy.get(`[data-cy="answer"]`).select('Q4');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        explain = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', explainPageUrlPattern);
    });
  });
});
