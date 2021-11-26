package com.epam.esm.audit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.time.ZonedDateTime;

import static com.epam.esm.audit.Operation.CREATE;
import static com.epam.esm.audit.Operation.DELETE;
import static com.epam.esm.audit.Operation.UPDATE;
import static java.time.ZoneOffset.UTC;

@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AuditListener {

    private static final Logger logger = LogManager.getLogger();

    private static final String CREATED_MESSAGE = "ENTITY CREATED: %s";
    private static final String UPDATED_MESSAGE = "ENTITY UPDATED: %s";
    private static final String DELETED_MESSAGE = "ENTITY DELETED: %s";

    private EntityManager entityManager;

    @Lazy
    public AuditListener(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostPersist
    public void postPersist(Object entity) {
        String auditMessage = String.format(CREATED_MESSAGE, entity);
        createRecord(entity, CREATE, auditMessage);
    }

    @PostUpdate
    public void postUpdate(Object entity) {
        String auditMessage = String.format(UPDATED_MESSAGE, entity);
        createRecord(entity, UPDATE, auditMessage);
    }

    @PostRemove
    public void postRemove(Object entity) {
        String auditMessage = String.format(DELETED_MESSAGE, entity);
        createRecord(entity, DELETE, auditMessage);
    }

    private void createRecord(Object entity, Operation operation, String auditMessage) {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setEntityName(entity.getClass().getSimpleName());
        auditEntity.setDate(ZonedDateTime.now(UTC));
        auditEntity.setOperation(operation);

        try {
            entityManager.getTransaction().begin();
            this.entityManager.persist(auditEntity);
            entityManager.getTransaction().commit();
            if (auditEntity.getId() != null) {
                logger.info(auditMessage);
            } else {
                logger.warn("Operation {} is not registered", operation);
            }
        } catch (PersistenceException | IllegalStateException e) {
            logger.error("AuditListener exception: {}, class: {}", e.getMessage(), e.getClass());
        }
    }
}
