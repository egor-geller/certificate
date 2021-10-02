package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private static final String ID_PARAMETER = "id";
    private static final String NAME_PARAMETER = "name";
    private static final String CERTIFICATE_ID_PARAMETER = "certificate_id";

    private static final String SELECT_ALL_TAGS = """
            SELECT id, name FROM tag
            """;

    private static final String SELECT_TAG_BY_ID = """
            SELECT id, name FROM tag WHERE id = :id
            """;

    private static final String SELECT_TAG_BY_NAME = """
            SELECT id, name FROM tag WHERE name = :name
            """;

    private static final String SELECT_TAG_BY_CERTIFICATE = """
            SELECT id, name FROM tag INNER JOIN gift_certificate_has_tag AS gt
            ON tag.id = gt.tag_id
            WHERE gt.gift_certificate_id = :certificate_id
            """;

    private static final String INSERT_TAG = """
            INSERT INTO tag (name) VALUES (:name)
            """;

    private static final String DELETE_TAG = """
            DELETE FROM tag WHERE id = :id
            """;

    private final RowMapper<Tag> rowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TagRepositoryImpl(RowMapper<Tag> rowMapper, DataSource dataSource) {
        this.rowMapper = rowMapper;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Tag> findAll() {
        return namedParameterJdbcTemplate.query(SELECT_ALL_TAGS, rowMapper);
    }

    @Override
    public Optional<Tag> findById(Long id) {
        SqlParameterSource in = new MapSqlParameterSource().addValue(ID_PARAMETER, id);

        List<Tag> tagList = namedParameterJdbcTemplate.query(SELECT_TAG_BY_ID, in ,rowMapper);

        return Optional.ofNullable(tagList.size() == 1 ? tagList.get(0) : null);
    }

    @Override
    public Optional<Tag> findByName(String tagName) {
        SqlParameterSource in = new MapSqlParameterSource().addValue(NAME_PARAMETER, tagName);

        List<Tag> tagList = namedParameterJdbcTemplate.query(SELECT_TAG_BY_NAME, in ,rowMapper);

        return Optional.ofNullable(tagList.size() == 1 ? tagList.get(0) : null);
    }

    @Override
    public List<Tag> findByCertificateId(Long id) {
        SqlParameterSource in = new MapSqlParameterSource().addValue(CERTIFICATE_ID_PARAMETER, id);

        return namedParameterJdbcTemplate.query(SELECT_TAG_BY_CERTIFICATE, in, rowMapper);
    }

    @Override
    public void createTag(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource in = new MapSqlParameterSource().addValue(NAME_PARAMETER, tag.getName());

        namedParameterJdbcTemplate.update(INSERT_TAG, in, keyHolder);
    }

    @Override
    public boolean deleteTag(Long id) {
        SqlParameterSource in = new MapSqlParameterSource().addValue(ID_PARAMETER, id);

        return namedParameterJdbcTemplate.update(DELETE_TAG, in) > 0;
    }
}
