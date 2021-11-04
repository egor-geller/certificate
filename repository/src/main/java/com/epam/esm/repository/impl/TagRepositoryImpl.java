package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.repositoryinterfaces.TagRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.builder.TagQueries.*;

@Repository
public class TagRepositoryImpl implements TagRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final PaginationContext paginationContext;

    public TagRepositoryImpl(EntityManager entityManager, PaginationContext paginationContext) {
        this.entityManager = entityManager;
        this.paginationContext = paginationContext;
    }

    @Override
    public List<Tag> findAll() {
        return entityManager.createQuery(SELECT_ALL_TAGS, Tag.class)
                .setFirstResult(paginationContext.getStartPage())
                .setMaxResults(paginationContext.getLengthOfContext())
                .getResultList();
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    public Optional<Tag> findByName(String tagName) {
        return entityManager.createQuery(SELECT_TAG_BY_NAME, Tag.class)
                .setParameter(1, tagName)
                .getResultList()
                .stream()
                .findFirst();

    }

    @Override
    public List<Tag> findByCertificateId(Long id) {
        return entityManager.createQuery(SELECT_TAG_BY_CERTIFICATE, Tag.class)
                .setFirstResult(paginationContext.getStartPage())
                .setMaxResults(paginationContext.getLengthOfContext())
                .setParameter(1, id)
                .getResultList();
    }

    @Transactional
    @Override
    public Tag create(Tag tag) {
        entityManager.persist(tag);
        entityManager.flush();
        return tag;
    }

    @Transactional
    @Override
    public void delete(Tag tag) {
        if (!entityManager.contains(tag)) {
            tag = entityManager.merge(tag);
        }
        entityManager.remove(tag);
    }
}
