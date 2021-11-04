package com.epam.esm.repository.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.DataException;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.builder.QueryBuilder;
import com.epam.esm.repository.repositoryinterfaces.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.builder.CertificateQueries.DELETE_TAG_FROM_CERTIFICATE;
import static com.epam.esm.repository.builder.CertificateQueries.INSERT_TAG_TO_CERTIFICATE;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final QueryBuilder queryBuilder;

    @Autowired
    public CertificateRepositoryImpl(EntityManager entityManager, QueryBuilder queryBuilder) {
        this.entityManager = entityManager;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public List<Certificate> find(SearchCriteria searchCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> query = queryBuilder.queryBuild(criteriaBuilder, searchCriteria);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Optional<Certificate> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Certificate.class, id));
    }

    @Transactional
    @Override
    public void attachTag(long certificateId, long tagId) {
        try {
            entityManager.createNativeQuery(INSERT_TAG_TO_CERTIFICATE)
                    .setParameter(1, certificateId)
                    .setParameter(2, tagId)
                    .executeUpdate();
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataException(tagId, certificateId);
        }
    }

    @Transactional
    @Override
    public void detachTag(long certificateId, long tagId) {
        try {
            entityManager.createNativeQuery(DELETE_TAG_FROM_CERTIFICATE)
                    .setParameter(1, certificateId)
                    .setParameter(2, tagId)
                    .executeUpdate();
        } catch (DataAccessException e) {
            throw new DataException(tagId, certificateId);
        }
    }

    @Transactional
    @Override
    public Certificate create(Certificate certificate) {
        entityManager.persist(certificate);
        entityManager.flush();
        return certificate;
    }

    @Transactional
    @Override
    public Certificate update(Certificate certificate) {
        return entityManager.merge(certificate);
    }

    @Transactional
    @Override
    public void delete(Certificate certificate) {
        if (!entityManager.contains(certificate)) {
            certificate = entityManager.merge(certificate);
        }
        entityManager.remove(certificate);
    }
}
