package com.epam.esm.repository.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.DataException;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.builder.QueryBuilder;
import com.epam.esm.repository.mapper.CertificateMapper;
import com.epam.esm.repository.repositoryinterfaces.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.Parameters.*;
import static com.epam.esm.repository.builder.CertificateQueries.*;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    private final CertificateMapper rowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CertificateRepositoryImpl(CertificateMapper rowMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        this.rowMapper = rowMapper;
        this.namedParameterJdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Certificate> find(SearchCriteria searchCriteria) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        QueryBuilder sqlClause = new QueryBuilder.Builder(searchCriteria, SELECT_ALL_CERTIFICATES)
                .buildWhereClause(parameters)
                .buildOrderByClause(searchCriteria)
                .build();
        return namedParameterJdbcTemplate.query(sqlClause.getNewString(), parameters, rowMapper);
    }

    @Override
    public Optional<Certificate> findById(Long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue(ID_PARAMETER, id);
        List<Certificate> certificates = namedParameterJdbcTemplate.query(SELECT_CERTIFICATE_BY_ID, parameterSource, rowMapper);

        return certificates.stream().findFirst();
    }

    @Override
    public boolean attachTag(long certificateId, long tagId) {
        int update;
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue(GIFT_CERTIFICATE_ID_PARAMETER, certificateId)
                .addValue(TAG_ID_PARAMETER, tagId);

        try {
            update = namedParameterJdbcTemplate.update(INSERT_TAG_TO_CERTIFICATE, parameterSource);
        } catch (DataAccessException e) {
            throw new DataException(e, tagId, certificateId);
        }
        return update > 0;
    }

    @Override
    public boolean detachTag(long certificateId, long tagId) {
        int update;
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue(GIFT_CERTIFICATE_ID_PARAMETER, certificateId)
                .addValue(TAG_ID_PARAMETER, tagId);

        try {
            update = namedParameterJdbcTemplate.update(DELETE_TAG_FROM_CERTIFICATE, parameterSource);
        } catch (DataAccessException e) {
            throw new DataException(e, tagId, certificateId);
        }
        return update > 0;
    }

    @Override
    public void create(Certificate certificate) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue(NAME_PARAMETER, certificate.getName())
                .addValue(DESCRIPTION_PARAMETER, certificate.getDescription())
                .addValue(PRICE_PARAMETER, certificate.getPrice())
                .addValue(DURATION_PARAMETER, certificate.getDuration().toDays())
                .addValue(CREATE_DATE_PARAMETER, certificate.getCreateDate())
                .addValue(LAST_UPDATE_PARAMETER, certificate.getLastUpdateDate());

        namedParameterJdbcTemplate.update(INSERT_CERTIFICATE, parameterSource);
    }

    @Override
    public boolean update(Certificate certificate) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue(ID_PARAMETER, certificate.getId())
                .addValue(NAME_PARAMETER, certificate.getName())
                .addValue(DESCRIPTION_PARAMETER, certificate.getDescription())
                .addValue(PRICE_PARAMETER, certificate.getPrice())
                .addValue(DURATION_PARAMETER, certificate.getDuration().toDays())
                .addValue(CREATE_DATE_PARAMETER, certificate.getCreateDate())
                .addValue(LAST_UPDATE_PARAMETER, certificate.getLastUpdateDate());

        return namedParameterJdbcTemplate.update(UPDATE_CERTIFICATE, parameterSource) > 0;
    }

    @Override
    public boolean delete(Long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue(ID_PARAMETER, id);

        return namedParameterJdbcTemplate.update(DELETE_CERTIFICATE, parameterSource) > 0;
    }
}
