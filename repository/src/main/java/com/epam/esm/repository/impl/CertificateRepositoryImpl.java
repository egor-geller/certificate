package com.epam.esm.repository.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.DataException;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.repositoryinterfaces.CertificateRepository;
import com.epam.esm.repository.builder.QueryBuilder;
import com.epam.esm.repository.builder.SortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.builder.CertificateQueries.*;
import static com.epam.esm.repository.Parameters.*;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    private final RowMapper<Certificate> rowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final QueryBuilder queryBuilder;

    public CertificateRepositoryImpl(RowMapper<Certificate> rowMapper,
                                     @Autowired DataSource dataSource,
                                     @Autowired QueryBuilder queryBuilder) {
        this.rowMapper = rowMapper;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.queryBuilder = queryBuilder;
    }

    @Override
    public List<Certificate> find(SearchCriteria searchCriteria) {

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        List<String> whereClauseFindByParameters = new ArrayList<>();
        List<String> orderByAscDesc = new ArrayList<>();

        queryBuilder.nameOfATag(whereClauseFindByParameters, parameters, searchCriteria.getTagName());
        queryBuilder.nameOfCertificate(whereClauseFindByParameters, parameters, searchCriteria.getCertificateName());
        queryBuilder.descriptionOfCertificate(whereClauseFindByParameters, parameters, searchCriteria.getCertificateDescription());
        queryBuilder.sortByCertificateName(orderByAscDesc, searchCriteria.getSortByName());
        queryBuilder.sortByCertificateCreationDate(orderByAscDesc, searchCriteria.getSortByCreateDate());

        String finishedQuery = queryBuilder.sqlBuild(whereClauseFindByParameters, orderByAscDesc, SELECT_ALL_CERTIFICATES);

        return namedParameterJdbcTemplate.query(finishedQuery, parameters, rowMapper);
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
            throw new DataException("There is a problem to attach: Tag id - " +
                    tagId + ", Certificate id - " + certificateId, e);
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
            throw new DataException("There is a problem to delete: Tag id - " +
                    tagId + ", Certificate id - " + certificateId, e);
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
