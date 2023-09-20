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

describe('Quiz e2e test', () => {
  const quizPageUrl = '/quiz';
  const quizPageUrlPattern = new RegExp('/quiz(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const quizSample = { code: 'since', question: 'jealously gee yippee' };

  let quiz;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/quizzes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/quizzes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/quizzes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (quiz) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/quizzes/${quiz.id}`,
      }).then(() => {
        quiz = undefined;
      });
    }
  });

  it('Quizzes menu should load Quizzes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('quiz');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Quiz').should('exist');
    cy.url().should('match', quizPageUrlPattern);
  });

  describe('Quiz page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(quizPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Quiz page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/quiz/new$'));
        cy.getEntityCreateUpdateHeading('Quiz');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', quizPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/quizzes',
          body: quizSample,
        }).then(({ body }) => {
          quiz = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/quizzes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [quiz],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(quizPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Quiz page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('quiz');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', quizPageUrlPattern);
      });

      it('edit button click should load edit Quiz page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Quiz');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', quizPageUrlPattern);
      });

      it('edit button click should load edit Quiz page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Quiz');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', quizPageUrlPattern);
      });

      it('last delete button click should delete instance of Quiz', () => {
        cy.intercept('GET', '/api/quizzes/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('quiz').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', quizPageUrlPattern);

        quiz = undefined;
      });
    });
  });

  describe('new Quiz page', () => {
    beforeEach(() => {
      cy.visit(`${quizPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Quiz');
    });

    it('should create an instance of Quiz', () => {
      cy.get(`[data-cy="code"]`).type('fussy');
      cy.get(`[data-cy="code"]`).should('have.value', 'fussy');

      cy.get(`[data-cy="question"]`).type('after');
      cy.get(`[data-cy="question"]`).should('have.value', 'after');

      cy.get(`[data-cy="example"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="example"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="selections"]`).type('yuck');
      cy.get(`[data-cy="selections"]`).should('have.value', 'yuck');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        quiz = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', quizPageUrlPattern);
    });
  });
});
