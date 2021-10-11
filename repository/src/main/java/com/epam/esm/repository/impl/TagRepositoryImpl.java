package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.mapper.TagMapper;
import com.epam.esm.repository.repositoryinterfaces.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.Parameters.*;
import static com.epam.esm.repository.builder.TagQueries.*;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final TagMapper rowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public TagRepositoryImpl(TagMapper rowMapper, DataSource dataSource) {
        this.rowMapper = rowMapper;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Tag> findAll() {
        return namedParameterJdbcTemplate.query(SELECT_ALL_TAGS, rowMapper);
    }

    @Override
    public Optional<Tag> findById(Long id) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue(ID_PARAMETER, id);

        List<Tag> tagList = namedParameterJdbcTemplate.query(SELECT_TAG_BY_ID, sqlParameterSource, rowMapper);

        return tagList.stream().findFirst();
    }

    @Override
    public Optional<Tag> findByName(String tagName) {
        SqlParameterSource in = new MapSqlParameterSource().addValue(NAME_PARAMETER, tagName);

        List<Tag> tagList = namedParameterJdbcTemplate.query(SELECT_TAG_BY_NAME, in, rowMapper);

        return tagList.stream().findFirst();
    }

    @Override
    public List<Tag> findByCertificateId(Long id) {
        SqlParameterSource in = new MapSqlParameterSource().addValue(CERTIFICATE_ID_PARAMETER, id);

        return namedParameterJdbcTemplate.query(SELECT_TAG_BY_CERTIFICATE, in, rowMapper);
    }

    @Override
    public void create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue(NAME_PARAMETER, tag.getName());

        namedParameterJdbcTemplate.update(INSERT_TAG, mapSqlParameterSource, keyHolder);
    }

    @Override
    public boolean delete(Long id) {
        SqlParameterSource in = new MapSqlParameterSource().addValue(ID_PARAMETER, id);

        return namedParameterJdbcTemplate.update(DELETE_TAG, in) > 0;
    }
}
