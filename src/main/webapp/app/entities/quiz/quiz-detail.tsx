import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './quiz.reducer';

export const QuizDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const quizEntity = useAppSelector(state => state.quiz.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="quizDetailsHeading">
          <Translate contentKey="examApp.quiz.detail.title">Quiz</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{quizEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="examApp.quiz.code">Code</Translate>
            </span>
          </dt>
          <dd>{quizEntity.code}</dd>
          <dt>
            <span id="question">
              <Translate contentKey="examApp.quiz.question">Question</Translate>
            </span>
          </dt>
          <dd>{quizEntity.question}</dd>
          <dt>
            <span id="example">
              <Translate contentKey="examApp.quiz.example">Example</Translate>
            </span>
          </dt>
          <dd>{quizEntity.example}</dd>
          <dt>
            <span id="selections">
              <Translate contentKey="examApp.quiz.selections">Selections</Translate>
            </span>
          </dt>
          <dd>{quizEntity.selections}</dd>
          <dt>
            <Translate contentKey="examApp.quiz.exam">Exam</Translate>
          </dt>
          <dd>{quizEntity.exam ? quizEntity.exam.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/quiz" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/quiz/${quizEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuizDetail;
