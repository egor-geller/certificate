package com.epam.esm.repository.builder;

import com.epam.esm.exception.QueryBuildException;
import com.epam.esm.repository.SearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.repository.Parameters.*;

@Component
public final class QueryBuilder {
    private String newString;

    public static class Builder {
        private final SearchCriteria searchCriteria;
        private String findBy;
        private String orderBy;
        MapSqlParameterSource parameters;
        private final String sqlString;

        public Builder(SearchCriteria searchCriteria, String sqlString) {
            this.sqlString = sqlString;
            this.searchCriteria = searchCriteria;
        }

        public Builder buildWhereClause(MapSqlParameterSource parameters) {
            List<String> list = new ArrayList<>();
            if (StringUtils.isNotEmpty(searchCriteria.getTagName())) {
                list.add("tag.name = :" + TAG_NAME_PARAMETER);
                parameters.addValue(TAG_NAME_PARAMETER, this.searchCriteria.getTagName());
            }

            if (StringUtils.isNotEmpty(searchCriteria.getCertificateName())) {
                list.add("cert.name = :" + NAME_PARAMETER);
                parameters.addValue(NAME_PARAMETER, this.searchCriteria.getCertificateName());
            }

            if (StringUtils.isNotEmpty(searchCriteria.getCertificateDescription())) {
                list.add("cert.description = :" + DESCRIPTION_PARAMETER);
                parameters.addValue(DESCRIPTION_PARAMETER, this.searchCriteria.getCertificateDescription());
            }

            StringBuilder stringBuilder = new StringBuilder();
            if (!list.isEmpty()) {
                stringBuilder.append("WHERE ");

                String collect = String.join(" AND ", list);
                stringBuilder.append(collect);
            }

            this.findBy = stringBuilder.toString();
            this.parameters = parameters;
            return this;
        }

        public Builder buildOrderByClause(SearchCriteria searchCriteria) {
            List<String> orderOf = new ArrayList<>();
            if (StringUtils.isNotEmpty(searchCriteria.getSortByNameOrCreationDate()) || searchCriteria.getOrderType() != null) {
                if (StringUtils.equalsIgnoreCase(searchCriteria.getSortByNameOrCreationDate(), "name")) {
                    orderOf.add("cert.name ");
                } else if (StringUtils.equalsIgnoreCase(searchCriteria.getSortByNameOrCreationDate(), "create_date")) {
                    orderOf.add("cert.create_date ");
                } else {
                    throw new QueryBuildException(searchCriteria.getSortByNameOrCreationDate());
                }
            }

            if (searchCriteria.getOrderType() != null) {
                orderOf.add(searchCriteria.getOrderType().toString());
            }

            StringBuilder stringBuilder = new StringBuilder();
            if (!orderOf.isEmpty()) {
                stringBuilder.append(" ORDER BY ");

                String join = String.join(" ", orderOf);
                stringBuilder.append(join);
            }

            this.orderBy = stringBuilder.toString();
            return this;
        }

        public QueryBuilder build() {
            QueryBuilder queryBuilder = new QueryBuilder();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.findBy).append(this.orderBy);
            queryBuilder.newString = String.format(sqlString, stringBuilder);

            return queryBuilder;
        }
    }


    private QueryBuilder() {
    }

    public String getNewString() {
        return newString;
    }
}
