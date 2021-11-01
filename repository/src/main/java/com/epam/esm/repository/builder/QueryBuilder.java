package com.epam.esm.repository.builder;

import com.epam.esm.entity.Certificate;
import com.epam.esm.repository.SearchCriteria;
import org.apache.commons.lang3.StringUtils;
import javax.persistence.criteria.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class QueryBuilder {

    private static final String TAGS = "tags";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "createDate";
    private static final String LAST_UPDATE_DATE = "lastUpdateDate";
    private static final String NAME_EXPRESSION = "name";
    private static final String CREATE_DATE_EXPRESSION = "create_date";
    private static final String PARTIAL_STRING = "%%%s%%";
    private CriteriaQuery<Certificate> query;

    public static class Builder {
        private final SearchCriteria searchCriteria;
        private CriteriaQuery<Certificate> query;
        private final CriteriaBuilder criteriaBuilder;
        private final Root<Certificate> certificateRoot;
        private final Join<Object, Object> join;
        List<Predicate> predicateList = new ArrayList<>();

        public Builder(SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder) {
            this.searchCriteria = searchCriteria;
            this.criteriaBuilder = criteriaBuilder;
            query = criteriaBuilder.createQuery(Certificate.class);
            certificateRoot = query.from(Certificate.class);
            join = certificateRoot.join(TAGS, JoinType.LEFT);
            query = query.select(certificateRoot);
        }

        public Builder buildQueryClause() {
            String parameter;
            //Condition by certificate name
            createConditionByParameter(searchCriteria.getCertificateName(), NAME);

            //Condition by certificate description
            createConditionByParameter(searchCriteria.getCertificateDescription(), DESCRIPTION);

            //Condition on which parameter to sort by
            parameter = createConditionBySortParameter();

            //Condition by order type
            Order order = orderBy(parameter);
            if (order != null) {
                query.orderBy(order);
            }

            this.query = query.select(certificateRoot).distinct(true);
            return this;
        }

        public Builder findNumberOfTags() {
            //Condition to find number of tags if exists
            if (searchCriteria.getTagList() != null) {
                Predicate inListPredicate = join.get(NAME).in(searchCriteria.getTagList());
                predicateList.add(inListPredicate);

                this.query = query.where(predicateList.toArray(new Predicate[0]))
                        .groupBy(
                                certificateRoot.get(ID),
                                certificateRoot.get(NAME),
                                certificateRoot.get(DESCRIPTION),
                                certificateRoot.get(PRICE),
                                certificateRoot.get(DURATION),
                                certificateRoot.get(CREATE_DATE),
                                certificateRoot.get(LAST_UPDATE_DATE)
                        ).having(
                                criteriaBuilder.equal(
                                        criteriaBuilder.countDistinct(join.get(ID)),
                                        searchCriteria.getTagList().size()
                                )
                        );
            } else {
                this.query = query.where(predicateList.toArray(new Predicate[0]));
            }
            return this;
        }

        public QueryBuilder build(){
            QueryBuilder queryBuilder = new QueryBuilder();
            queryBuilder.query = this.query;
            return queryBuilder;
        }

        private void createConditionByParameter(String parameter, String attributeName) {
            if (StringUtils.isNotEmpty(parameter)) {
                String partialName = String.format(PARTIAL_STRING, parameter);
                Predicate predicate = criteriaBuilder.like(certificateRoot.get(attributeName), partialName);
                predicateList.add(predicate);
            }
        }

        private String createConditionBySortParameter() {
            String parameter = "";
            if (StringUtils.isNotEmpty(searchCriteria.getSortByParameter())) {
                if (searchCriteria.getSortByParameter().equals(CREATE_DATE_EXPRESSION)) {
                    parameter = CREATE_DATE;
                } else if (searchCriteria.getSortByParameter().equals(NAME_EXPRESSION)) {
                    parameter = NAME;
                }
            }
            return parameter;
        }

        private Order orderBy(String parameter) {
            Order order = null;
            if (searchCriteria.getOrderType() != null) {
                order = switch (searchCriteria.getOrderType()) {
                    case ASC -> criteriaBuilder.asc(certificateRoot.get(parameter));
                    case DESC -> criteriaBuilder.desc(certificateRoot.get(parameter));
                };
            }
            return order;
        }
    }

    private QueryBuilder() {
    }

    public CriteriaQuery<Certificate> getQuery() {
        return query;
    }
}
