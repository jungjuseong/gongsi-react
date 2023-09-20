import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAgency } from 'app/shared/model/agency.model';
import { getEntities as getAgencies } from 'app/entities/agency/agency.reducer';
import { ILicense } from 'app/shared/model/license.model';
import { getEntities as getLicenses } from 'app/entities/license/license.reducer';
import { IExam } from 'app/shared/model/exam.model';
import { ExamType } from 'app/shared/model/enumerations/exam-type.model';
import { getEntity, updateEntity, createEntity, reset } from './exam.reducer';

export const ExamUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const agencies = useAppSelector(state => state.agency.entities);
  const licenses = useAppSelector(state => state.license.entities);
  const examEntity = useAppSelector(state => state.exam.entity);
  const loading = useAppSelector(state => state.exam.loading);
  const updating = useAppSelector(state => state.exam.updating);
  const updateSuccess = useAppSelector(state => state.exam.updateSuccess);
  const examTypeValues = Object.keys(ExamType);

  const handleClose = () => {
    navigate('/exam');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAgencies({}));
    dispatch(getLicenses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...examEntity,
      ...values,
      implementingAgency: agencies.find(it => it.id.toString() === values.implementingAgency.toString()),
      license: licenses.find(it => it.id.toString() === values.license.toString()),
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
          examType: 'KOREAN',
          ...examEntity,
          implementingAgency: examEntity?.implementingAgency?.id,
          license: examEntity?.license?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="examApp.exam.home.createOrEditLabel" data-cy="ExamCreateUpdateHeading">
            <Translate contentKey="examApp.exam.home.createOrEditLabel">Create or edit a Exam</Translate>
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
                  id="exam-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('examApp.exam.title')}
                id="exam-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('examApp.exam.examType')}
                id="exam-examType"
                name="examType"
                data-cy="examType"
                type="select"
              >
                {examTypeValues.map(examType => (
                  <option value={examType} key={examType}>
                    {translate('examApp.ExamType.' + examType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('examApp.exam.effectiveDate')}
                id="exam-effectiveDate"
                name="effectiveDate"
                data-cy="effectiveDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="exam-implementingAgency"
                name="implementingAgency"
                data-cy="implementingAgency"
                label={translate('examApp.exam.implementingAgency')}
                type="select"
              >
                <option value="" key="0" />
                {agencies
                  ? agencies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="exam-license" name="license" data-cy="license" label={translate('examApp.exam.license')} type="select">
                <option value="" key="0" />
                {licenses
                  ? licenses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/exam" replace color="info">
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

export default ExamUpdate;
