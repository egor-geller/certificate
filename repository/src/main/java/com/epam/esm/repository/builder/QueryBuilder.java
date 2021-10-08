package com.epam.esm.repository.builder;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

import static com.epam.esm.repository.Parameters.*;

@Component
public class QueryBuilder {

    private QueryBuilder() {
    }

    public void nameOfATag(List<String> findBy, MapSqlParameterSource parameters, String tagName) {
        if (tagName != null && !tagName.isEmpty()) {
            findBy.add("tag.name = :" + TAG_NAME_PARAMETER);
            parameters.addValue(TAG_NAME_PARAMETER, tagName);
        }
    }

    public void nameOfCertificate(List<String> findBy, MapSqlParameterSource parameters, String certificateName) {
        if (!certificateName.isEmpty()) {
            findBy.add("gift_certificate.name = :" + NAME_PARAMETER);
            parameters.addValue(NAME_PARAMETER, certificateName);
        }
    }

    public void descriptionOfCertificate(List<String> findBy, MapSqlParameterSource parameters, String certificateDescription) {
        if (!certificateDescription.isEmpty()) {
            findBy.add("gift_certificate.description = :" + DESCRIPTION_PARAMETER);
            parameters.addValue(DESCRIPTION_PARAMETER, certificateDescription);
        }
    }

    public void sortByCertificateName(List<String> orderBy, SortType sortByName) {
        if (sortByName != null) {
            orderBy.add("gift_certificate.name " + sortByName.name());
        }
    }

    public void sortByCertificateCreationDate(List<String> orderBy, SortType sortByCreateDate) {
        if (sortByCreateDate != null) {
            orderBy.add("gift_certificate.create_date " + sortByCreateDate.name());
        }
    }

    public String sqlBuild(List<String> where, List<String> order, String sqlQuery) {
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

        String sqlBuilder = whereSql.append(orderSql).toString();

        return String.format(sqlQuery, sqlBuilder);
    }
}
