package com.epam.esm.validator;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.SearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class CertificateValidator {

    private CertificateValidator() {
    }

    private static final String NAME_REGEX = "[a-zA-Z0-9.,'?!\"]{5,30}";
    private static final String DESCRIPTION_REGEX = "[a-zA-Z0-9.,'?!\"]{5,100}";
    private static final String TAG_NAME_REGEX = "[a-zA-Z0-9.,'?!\"]{2,30}";

    public boolean isCertificateDtoValid(CertificateDto certificate) {
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        Duration duration = certificate.getDuration();
        List<String> tagList = certificate.getTagList();

        boolean areParamsEmpty = areParamsEmpty(name, description, String.valueOf(price), String.valueOf(duration));

        boolean tagListIsEmpty = tagList.stream().allMatch(String::isEmpty);

        return !tagListIsEmpty && !areParamsEmpty && isValidName(name) && isValidDescription(description)
                && isValidPrice(price) && isValidDuration(duration) && !isTagListInvalid(tagList);
    }

    public boolean areParamsEmpty(String... params) {
        return Arrays.stream(params).allMatch(String::isEmpty);
    }

    public void validateSearchCriteriaEmpty(SearchCriteria searchCriteria) {
        if (StringUtils.isNoneEmpty(searchCriteria.getTagName())
                && StringUtils.isNotEmpty(searchCriteria.getCertificateName())
                && StringUtils.isNotEmpty(searchCriteria.getCertificateDescription())
                && searchCriteria.getSortByName() != null && !StringUtils.isNotEmpty(searchCriteria.getSortByName().toString())
                && searchCriteria.getSortByCreateDate() != null && !StringUtils.isNotEmpty(searchCriteria.getSortByCreateDate().toString())) {

            throw new InvalidEntityException(CertificateValidator.class);
        }
    }


    private boolean isTagListInvalid(List<String> tagList) {
        return tagList.stream().allMatch(tagName -> Pattern.matches(TAG_NAME_REGEX, tagName));
    }

    private boolean isValidName(String name) {
        return Pattern.matches(NAME_REGEX, name);
    }

    private boolean isValidDescription(String description) {
        return Pattern.matches(DESCRIPTION_REGEX, description);
    }

    private boolean isValidPrice(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isValidDuration(Duration duration) {
        return duration.toDays() > 0;
    }
}
