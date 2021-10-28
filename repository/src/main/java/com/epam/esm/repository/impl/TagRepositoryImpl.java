package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.repositoryinterfaces.TagRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.builder.TagQueries.*;

@Repository
public class TagRepositoryImpl implements TagRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public TagRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Tag> findAll() {
        return entityManager.createQuery(SELECT_ALL_TAGS, Tag.class).getResultList();
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return Optional.of(entityManager.find(Tag.class, id));
    }

    @Override
    public Optional<Tag> findByName(String tagName) {
        return Optional.of(entityManager.createQuery(SELECT_TAG_BY_NAME, Tag.class)
                .setParameter(1, tagName)
                .getSingleResult());
    }

    @Override
    public List<Tag> findByCertificateId(Long id) {
        return entityManager.createQuery(SELECT_TAG_BY_CERTIFICATE, Tag.class)
                .setParameter(1, id)
                .getResultList();
    }

    @Override
    public Tag create(Tag tag) {
        entityManager.persist(tag);
        Optional<Tag> optionalTag = findByName(tag.getName());

        return optionalTag.orElse(tag);

    }

    @Override
    public void delete(Tag tag) {
        entityManager.remove(tag);
    }
}
