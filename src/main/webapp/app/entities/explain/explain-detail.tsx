import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './explain.reducer';

export const ExplainDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const explainEntity = useAppSelector(state => state.explain.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="explainDetailsHeading">
          <Translate contentKey="examApp.explain.detail.title">Explain</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{explainEntity.id}</dd>
          <dt>
            <span id="answer">
              <Translate contentKey="examApp.explain.answer">Answer</Translate>
            </span>
          </dt>
          <dd>{explainEntity.answer}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="examApp.explain.description">Description</Translate>
            </span>
          </dt>
          <dd>{explainEntity.description}</dd>
          <dt>
            <Translate contentKey="examApp.explain.quiz">Quiz</Translate>
          </dt>
          <dd>{explainEntity.quiz ? explainEntity.quiz.code : ''}</dd>
        </dl>
        <Button tag={Link} to="/explain" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/explain/${explainEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExplainDetail;
