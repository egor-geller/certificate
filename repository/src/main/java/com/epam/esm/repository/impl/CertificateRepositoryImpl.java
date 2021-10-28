package com.epam.esm.repository.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.DataException;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.repositoryinterfaces.CertificateRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.builder.CertificateQueries.DELETE_TAG_FROM_CERTIFICATE;
import static com.epam.esm.repository.builder.CertificateQueries.INSERT_TAG_TO_CERTIFICATE;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    private static final String TAGS = "tags";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PARAMETER = "parameter";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "createDate";
    private static final String LAST_UPDATE_DATE = "lastUpdateDate";
    private static final String PARTIAL_STRING = "%%%s%%";

    @PersistenceContext
    private EntityManager entityManager;

    public CertificateRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Certificate> find(SearchCriteria searchCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> query = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> certificateRoot = query.from(Certificate.class);
        List<Predicate> predicateList = new ArrayList<>();
        Join<Object, Object> join = certificateRoot.join(TAGS, JoinType.LEFT);
        query = query.select(certificateRoot);

        //Condition by certificate name
        if (StringUtils.isNotEmpty(searchCriteria.getCertificateName())) {
            String partialName = String.format(PARTIAL_STRING, searchCriteria.getCertificateName());
            Predicate predicate = criteriaBuilder.like(certificateRoot.get(NAME), partialName);
            predicateList.add(predicate);
        }

        //Condition by certificate description
        if (StringUtils.isNotEmpty(searchCriteria.getCertificateDescription())) {
            String partialName = String.format(PARTIAL_STRING, searchCriteria.getCertificateDescription());
            Predicate predicate = criteriaBuilder.like(certificateRoot.get(DESCRIPTION), partialName);
            predicateList.add(predicate);
        }

        //Condition on which parameter to sort by
        if (StringUtils.isNotEmpty(searchCriteria.getSortByParameter())) {
            String partialName = String.format(PARTIAL_STRING, searchCriteria.getSortByParameter());
            Predicate predicate = criteriaBuilder.like(certificateRoot.get(PARAMETER), partialName);
            predicateList.add(predicate);
        }

        //Condition by order type
        if (searchCriteria.getOrderType() != null) {
            Order order = switch (searchCriteria.getOrderType()) {
                case ASC -> criteriaBuilder.asc(certificateRoot.get(PARAMETER));
                case DESC -> criteriaBuilder.desc(certificateRoot.get(PARAMETER));
            };
            query.orderBy(order);
        }

        query = query.select(certificateRoot).distinct(true);

        //Condition to find number of tags if exists
        if (searchCriteria.getTagList() != null) {
            Predicate inListPredicate = join.get(NAME).in(searchCriteria.getTagList());
            predicateList.add(inListPredicate);

            query = query.where(predicateList.toArray(new Predicate[0]))
                    .groupBy(
                            certificateRoot.get(ID),
                            certificateRoot.get(NAME),
                            certificateRoot.get(DESCRIPTION),
                            certificateRoot.get(PRICE),
                            certificateRoot.get(DURATION),
                            certificateRoot.get(CREATE_DATE),
                            certificateRoot.get(LAST_UPDATE_DATE)
                    ).having(
                            criteriaBuilder.equal(
                                    criteriaBuilder.countDistinct(join.get(ID)),
                                    searchCriteria.getTagList().size()
                            )
                    );
        } else {
            query = query.where(predicateList.toArray(new Predicate[0]));
        }

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Optional<Certificate> findById(Long id) {
        return Optional.of(entityManager.find(Certificate.class, id));
    }

    @Override
    public void attachTag(long certificateId, long tagId) {
        try {
            entityManager.createNativeQuery(INSERT_TAG_TO_CERTIFICATE)
                    .setParameter(1, certificateId)
                    .setParameter(2, tagId);
        } catch (DataAccessException e) {
            throw new DataException(tagId, certificateId);
        }
    }

    @Override
    public void detachTag(long certificateId, long tagId) {
        try {
            entityManager.createNativeQuery(DELETE_TAG_FROM_CERTIFICATE)
                    .setParameter(1, certificateId)
                    .setParameter(2, tagId);
        } catch (DataAccessException e) {
            throw new DataException(tagId, certificateId);
        }
    }

    @Override
    public Certificate create(Certificate certificate) {
        entityManager.persist(certificate);
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setCertificateName(certificate.getName());
        List<Certificate> certificateList = find(searchCriteria);
        return certificateList.stream().findFirst().orElse(certificate);
    }

    @Override
    public Certificate update(Certificate certificate) {
        return entityManager.merge(certificate);
    }

    @Override
    public void delete(Certificate certificate) {
        entityManager.remove(certificate);
    }
}
