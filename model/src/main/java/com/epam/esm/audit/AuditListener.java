package com.epam.esm.audit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import java.time.ZonedDateTime;

import static com.epam.esm.audit.Operation.*;
import static java.time.ZoneOffset.UTC;

@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AuditListener {

    private static final Logger logger = LogManager.getLogger();

    private static final String CREATED_MESSAGE = "ENTITY CREATED: %s";
    private static final String UPDATED_MESSAGE = "ENTITY UPDATED: %s";
    private static final String DELETED_MESSAGE = "ENTITY DELETED: %s";

    @PersistenceContext
    private EntityManager entityManager;

    public AuditListener(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostPersist
    public void postPersist(Object entity) {
        String auditMessage = String.format(CREATED_MESSAGE, entity);
        logger.info(auditMessage);

        createRecord(entity, CREATE);
    }

    @PostUpdate
    public void postUpdate(Object entity) {
        String auditMessage = String.format(UPDATED_MESSAGE, entity);
        logger.info(auditMessage);

        createRecord(entity, UPDATE);
    }

    @PostRemove
    public void postRemove(Object entity) {
        String auditMessage = String.format(DELETED_MESSAGE, entity);
        logger.info(auditMessage);

        createRecord(entity, DELETE);
    }

    private void createRecord(Object entity, Operation operation) {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setEntityName(entity.getClass().getSimpleName());
        auditEntity.setDate(ZonedDateTime.now(UTC));
        auditEntity.setOperation(operation);

        entityManager.persist(auditEntity);
    }
}