package com.epam.esm.repository.builder;

import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.QueryBuildException;
import com.epam.esm.repository.SearchCriteria;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public final class QueryBuilder {

    private static final Logger logger = LogManager.getLogger();

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

    private QueryBuilder() {
    }

    public CriteriaQuery<Certificate> queryBuild(CriteriaBuilder criteriaBuilder, SearchCriteria searchCriteria) {
        CriteriaQuery<Certificate> query = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> certificateRoot = query.from(Certificate.class);
        List<Predicate> predicateList = new ArrayList<>();
        Join<Object, Object> join = certificateRoot.join(TAGS, JoinType.LEFT);
        query = query.select(certificateRoot);
        //Condition by certificate name
        createWhereCondition(searchCriteria.getCertificateName(), NAME, criteriaBuilder, certificateRoot, predicateList);
        //Condition by certificate description
        createWhereCondition(searchCriteria.getCertificateDescription(), DESCRIPTION,
                criteriaBuilder, certificateRoot, predicateList);
        //Condition on which parameter to sort by with order type
        String sortCondition = createSortCondition(searchCriteria.getSortByParameter());
        //Condition by order type
        Order order = orderBy(searchCriteria.getOrderType(), sortCondition, criteriaBuilder, certificateRoot);
        if (order != null) {
            query.orderBy(order);
        }
        query = query.select(certificateRoot).distinct(true);
        //Condition to find number of tags if exists
        query = createConditionForTags(searchCriteria.getTagList(), predicateList, join, query, certificateRoot);
        return query;
    }

    private void createWhereCondition(String searchCriteria,
                                      String attributeValue,
                                      CriteriaBuilder criteriaBuilder,
                                      Root<Certificate> certificateRoot,
                                      List<Predicate> predicateList) {
        if (StringUtils.isNotEmpty(searchCriteria)) {
            String partialName = String.format(PARTIAL_STRING, searchCriteria);
            Predicate predicate = criteriaBuilder.like(certificateRoot.get(attributeValue), partialName);
            predicateList.add(predicate);
        }
        logger.info("search: {}", searchCriteria);
    }

    private String createSortCondition(String searchCriteria) {
        String parameter = "";
        if (StringUtils.isNotEmpty(searchCriteria)) {
            if (searchCriteria.equals(NAME_EXPRESSION)) {
                parameter = NAME;
            } else if (searchCriteria.equals(CREATE_DATE_EXPRESSION)) {
                parameter = CREATE_DATE;
            }
        }
        return parameter;
    }

    private Order orderBy(SortType sortType,
                          String parameter,
                          CriteriaBuilder criteriaBuilder,
                          Root<Certificate> certificateRoot) {
        Order order = null;
        if (sortType != null) {
            if (sortType.name().equals(SortType.DESC.name())) {
                order = criteriaBuilder.desc(certificateRoot.get(parameter));
            } else {
                order = criteriaBuilder.asc(certificateRoot.get(parameter));
            }
        }
        return order;
    }

    private CriteriaQuery<Certificate> createConditionForTags(List<String> tagList,
                                                              List<Predicate> predicateList,
                                                              Join<Object, Object> join,
                                                              CriteriaQuery<Certificate> query,
                                                              Root<Certificate> certificateRoot) {

        if (tagList != null) {
            Predicate inListPredicate = join.get(NAME).in(tagList);
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
                    );
        } else {
            query = query.where(predicateList.toArray(new Predicate[0]));
        }
        return query;
    }
}

