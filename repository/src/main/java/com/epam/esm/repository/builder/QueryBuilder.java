package com.epam.esm.repository.builder;

import com.epam.esm.repository.SearchCriteria;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.epam.esm.repository.Parameters.*;

@Component
public final class QueryBuilder {
    String newString;

    public static class Builder {
        private final SearchCriteria searchCriteria;
        private String findBy;
        private String orderBy;
        MapSqlParameterSource parameters;
        String sqlString;

        public Builder(SearchCriteria searchCriteria, String sqlString) {
            this.sqlString = sqlString;
            this.searchCriteria = searchCriteria;
        }

        public Builder findBy(MapSqlParameterSource parameters) {
            List<String> list = new ArrayList<>();
            if (this.searchCriteria.getTagName() != null && !this.searchCriteria.getTagName().isEmpty()) {
                list.add("tag.name = :" + TAG_NAME_PARAMETER);
                parameters.addValue(TAG_NAME_PARAMETER, this.searchCriteria.getTagName());
            }

            if (this.searchCriteria.getCertificateName() != null && !this.searchCriteria.getCertificateName().isEmpty()) {
                list.add("cert.name = :" + NAME_PARAMETER);
                parameters.addValue(NAME_PARAMETER, this.searchCriteria.getCertificateName());
            }

            if (this.searchCriteria.getCertificateDescription() != null && !this.searchCriteria.getCertificateDescription().isEmpty()) {
                list.add("cert.description = :" + DESCRIPTION_PARAMETER);
                parameters.addValue(DESCRIPTION_PARAMETER, this.searchCriteria.getCertificateDescription());
            }

            StringBuilder stringBuilder = new StringBuilder();
            if (!list.isEmpty()) {
                stringBuilder.append("WHERE ");
                Iterator<String> iterator = list.iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    stringBuilder.append(next);

                    if (iterator.hasNext()) {
                        stringBuilder.append(" AND ");
                    }
                }
            }


            this.findBy = stringBuilder.toString();
            this.parameters = parameters;
            return this;
        }

        public Builder orderBy(SearchCriteria searchCriteria) {
            List<String> orderOf = new ArrayList<>();
            if (searchCriteria.getSortByName() != null) {
                orderOf.add("cert.name " + searchCriteria.getSortByName().toString());
            }

            if (searchCriteria.getSortByCreateDate() != null) {
                orderOf.add("cert.create_date " + searchCriteria.getSortByCreateDate().toString());
            }

            StringBuilder stringBuilder = new StringBuilder();
            if (!orderOf.isEmpty()) {
                stringBuilder.append(" ORDER BY ");
                Iterator<String> iterator = orderOf.iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    stringBuilder.append(next);

                    if (iterator.hasNext()) {
                        stringBuilder.append(" ,");
                    }
                }
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
