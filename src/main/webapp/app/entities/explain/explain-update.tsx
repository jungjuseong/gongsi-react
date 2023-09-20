import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IQuiz } from 'app/shared/model/quiz.model';
import { getEntities as getQuizzes } from 'app/entities/quiz/quiz.reducer';
import { IExplain } from 'app/shared/model/explain.model';
import { AnswerType } from 'app/shared/model/enumerations/answer-type.model';
import { getEntity, updateEntity, createEntity, reset } from './explain.reducer';

export const ExplainUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const quizzes = useAppSelector(state => state.quiz.entities);
  const explainEntity = useAppSelector(state => state.explain.entity);
  const loading = useAppSelector(state => state.explain.loading);
  const updating = useAppSelector(state => state.explain.updating);
  const updateSuccess = useAppSelector(state => state.explain.updateSuccess);
  const answerTypeValues = Object.keys(AnswerType);

  const handleClose = () => {
    navigate('/explain');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getQuizzes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...explainEntity,
      ...values,
      quiz: quizzes.find(it => it.id.toString() === values.quiz.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          answer: 'Q1',
          ...explainEntity,
          quiz: explainEntity?.quiz?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="examApp.explain.home.createOrEditLabel" data-cy="ExplainCreateUpdateHeading">
            <Translate contentKey="examApp.explain.home.createOrEditLabel">Create or edit a Explain</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="explain-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('examApp.explain.answer')} id="explain-answer" name="answer" data-cy="answer" type="select">
                {answerTypeValues.map(answerType => (
                  <option value={answerType} key={answerType}>
                    {translate('examApp.AnswerType.' + answerType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('examApp.explain.description')}
                id="explain-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField id="explain-quiz" name="quiz" data-cy="quiz" label={translate('examApp.explain.quiz')} type="select">
                <option value="" key="0" />
                {quizzes
                  ? quizzes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.code}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/explain" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ExplainUpdate;
