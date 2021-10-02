package com.epam.esm.repository.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.SortType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    private static final String ID_PARAMETER = "id";
    private static final String NAME_PARAMETER = "name";
    private static final String DESCRIPTION_PARAMETER = "description";
    private static final String PRICE_PARAMETER = "price";
    private static final String DURATION_PARAMETER = "duration";
    private static final String CREATE_DATE_PARAMETER = "create_date";
    private static final String LAST_UPDATE_PARAMETER = "last_update_date";
    private static final String TAG_ID_PARAMETER = "tag_id";
    private static final String TAG_NAME_PARAMETER = "tag_name";
    private static final String GIFT_CERTIFICATE_ID_PARAMETER = "gift_certificate_id";

    private static final String SELECT_ALL_CERTIFICATES = """
            SELECT cert.id, cert.name, description, price, duration, create_date, last_update_date
            FROM gift_certificate AS cert
            LEFT OUTER JOIN gift_certificate_has_tag gcht
            ON cert.id = gcht.gift_certificate_id
            LEFT OUTER JOIN tag
            ON gcht.tag_id = tag.id
            %s;
            """;

    private static final String SELECT_CERTIFICATE_BY_ID = """
            SELECT ID, NAME, DESCRIPTION, PRICE, DURATION, CREATE_DATE, LAST_UPDATE_DATE FROM gift_certificate
            WHERE id = :id;
            """;

    private static final String INSERT_TAG_TO_CERTIFICATE = """
            INSERT INTO gift_certificate_has_tag (gift_certificate_id, tag_id)
            VALUES (:id, :tag_id);
            """;

    private static final String DELETE_TAG_FROM_CERTIFICATE = """
            DELETE FROM gift_certificate_has_tag
            WHERE gift_certificate_id = :gift_certificate_id AND tag_id = :tag_id;
            """;

    private static final String INSERT_CERTIFICATE = """
            INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
            VALUES (:name, :description, :price, :duration, :create_date, :last_update_date)
            """;

    private static final String UPDATE_CERTIFICATE = """
            UPDATE gift_certificate
            SET name = :name, description = :description, price = :price, duration = :duration, last_update_date = :last_update_date
            WHERE id = :id;
            """;

    private static final String DELETE_CERTIFICATE = """
            DELETE FROM gift_certificate
            WHERE id = :id;
            """;

    private final RowMapper<Certificate> rowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CertificateRepositoryImpl(DataSource dataSource, RowMapper<Certificate> rowMapper) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Certificate> find(String tagName, String certificateName, String certificateDescription
            , SortType sortByName, SortType sortByCreateDate) {

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        List<String> findBy = new ArrayList<>();
        List<String> orderBy = new ArrayList<>();

        if (!tagName.isEmpty()) {
            findBy.add("tag.name = :" + TAG_NAME_PARAMETER);
            parameters.addValue(TAG_NAME_PARAMETER, tagName);
        }

        if (!certificateName.isEmpty()) {
            findBy.add("gift_certificate.name = :" + NAME_PARAMETER);
            parameters.addValue(NAME_PARAMETER, certificateName);
        }

        if (!certificateDescription.isEmpty()) {
            findBy.add("gift_certificate.description = :" + DESCRIPTION_PARAMETER);
            parameters.addValue(DESCRIPTION_PARAMETER, certificateDescription);
        }

        if (sortByName != null) {
            orderBy.add("gift_certificate.name " + sortByName.name());
        }

        if (sortByCreateDate != null) {
            orderBy.add("gift_certificate.create_date " + sortByCreateDate.name());
        }

        String sqlBuilder = sqlBuild(findBy, orderBy);

        String finishedQuery = String.format(SELECT_ALL_CERTIFICATES, sqlBuilder);

        return namedParameterJdbcTemplate.query(finishedQuery, parameters, rowMapper);
    }

    @Override
    public Optional<Certificate> findById(long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue(ID_PARAMETER, id);
        List<Certificate> certificates = namedParameterJdbcTemplate.query(SELECT_CERTIFICATE_BY_ID, parameterSource, rowMapper);

        return Optional.ofNullable(certificates.size() == 1 ? certificates.get(0) : null);
    }

    @Override
    public boolean attachTag(long certificateId, long tagId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue(GIFT_CERTIFICATE_ID_PARAMETER, certificateId)
                .addValue(TAG_ID_PARAMETER, tagId);

        return namedParameterJdbcTemplate.update(INSERT_TAG_TO_CERTIFICATE, parameterSource) > 0;
    }

    @Override
    public boolean detachTag(long certificateId, long tagId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue(GIFT_CERTIFICATE_ID_PARAMETER, certificateId)
                .addValue(TAG_ID_PARAMETER, tagId);

        return namedParameterJdbcTemplate.update(DELETE_TAG_FROM_CERTIFICATE, parameterSource) > 0;
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
    public boolean delete(long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue(ID_PARAMETER, id);

        return namedParameterJdbcTemplate.update(DELETE_CERTIFICATE, parameterSource) > 0;
    }

    private String sqlBuild(List<String> where, List<String> order) {
        StringBuilder whereSql = new StringBuilder();
        StringBuilder orderSql = new StringBuilder();

        if (!where.isEmpty()) {
            whereSql = new StringBuilder("WHERE ");
        }

        if (!order.isEmpty()) {
            orderSql = new StringBuilder("ORDER BY ");
        }

        Iterator<String> itr = where.iterator();

        while (itr.hasNext()) {
            String findBy = itr.next();
            whereSql.append(findBy);

            if (itr.hasNext()) {
                whereSql.append(" AND ");
            }
        }

        itr = order.iterator();

        while (itr.hasNext()) {
            String orderBy = itr.next();
            orderSql.append(orderBy);

            if (itr.hasNext()) {
                orderSql.append(", ");
            }
        }

        return whereSql.append(orderSql).toString();
    }
}
