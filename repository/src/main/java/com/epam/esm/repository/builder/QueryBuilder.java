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

    public CriteriaQuery<Certificate> queryBuild(CriteriaBuilder criteriaBuilder, SearchCriteria searchCriteria) {
        String parameter = "";
        CriteriaQuery<Certificate> query = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> certificateRoot = query.from(Certificate.class);
        List<Predicate> predicateList = new ArrayList<>();
        Join<Object, Object> join = certificateRoot.join(TAGS, JoinType.LEFT);
        query = query.select(certificateRoot);

        //Condition by certificate name
        if (StringUtils.isNotEmpty(searchCriteria.getCertificateName())) {
            String partialName = String.format(PARTIAL_STRING, searchCriteria.getCertificateName());
            Predicate predicate = criteriaBuilder.like(certificateRoot.get(NAME), partialName);
            predicateList.add(predicate);
        }

        //Condition by certificate description
        if (StringUtils.isNotEmpty(searchCriteria.getCertificateDescription())) {
            String partialName = String.format(PARTIAL_STRING, searchCriteria.getCertificateDescription());
            Predicate predicate = criteriaBuilder.like(certificateRoot.get(DESCRIPTION), partialName);
            predicateList.add(predicate);
        }

        //Condition on which parameter to sort by
        if (StringUtils.isNotEmpty(searchCriteria.getSortByParameter())) {
            if (searchCriteria.getSortByParameter().equals(CREATE_DATE_EXPRESSION)) {
                parameter = CREATE_DATE;
            } else if (searchCriteria.getSortByParameter().equals(NAME_EXPRESSION)) {
                parameter = NAME;
            }
        }

        //Condition by order type
        if (searchCriteria.getOrderType() != null) {
            Order order = switch (searchCriteria.getOrderType()) {
                case ASC -> criteriaBuilder.asc(certificateRoot.get(parameter));
                case DESC -> criteriaBuilder.desc(certificateRoot.get(parameter));
            };
            query.orderBy(order);
        }

        query = query.select(certificateRoot).distinct(true);

        //Condition to find number of tags if exists
        if (searchCriteria.getTagList() != null) {
            Predicate inListPredicate = join.get(NAME).in(searchCriteria.getTagList());
            predicateList.add(inListPredicate);

            query = query.where(predicateList.toArray(new Predicate[0]))
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
            query = query.where(predicateList.toArray(new Predicate[0]));
        }

        return query;
    }
}
