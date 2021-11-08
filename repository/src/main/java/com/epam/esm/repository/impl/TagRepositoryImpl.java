package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.repositoryinterfaces.CreateRepository;
import com.epam.esm.repository.repositoryinterfaces.DeleteRepository;
import com.epam.esm.repository.repositoryinterfaces.TagRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.query.TagQueries.*;

@Repository
public class TagRepositoryImpl implements TagRepository, CreateRepository<Tag>, DeleteRepository<Tag> {

    @PersistenceContext
    private EntityManager entityManager;

    public TagRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Tag> findAll(PaginationContext paginationContext) {
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
    public List<Tag> findByCertificateId(PaginationContext paginationContext, Long id) {
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
