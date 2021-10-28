package com.epam.esm.validator;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.SearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class CertificateValidator {

    private CertificateValidator() {
    }

    private static final String NAME_REGEX = "[a-zA-Z0-9.,'?!\" ]{5,30}";
    private static final String DESCRIPTION_REGEX = "[a-zA-Z0-9.,'?!\" ]{5,100}";
    private static final String TAG_NAME_REGEX = "[a-zA-Z0-9.,'?!\" ]{2,30}";
    private static final String VALID_ID_REGEX = "\\^\\d+";

    public boolean isCertificateDtoValid(CertificateDto certificate) {
        boolean tagListIsEmpty;
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        Duration duration = certificate.getDuration();

        boolean areParamsEmpty = areParamsEmpty(name, description, String.valueOf(price), String.valueOf(duration));

        List<Tag> tagList;
        if (certificate.getTagList() == null) {
            return !areParamsEmpty && isValidName(name) && isValidDescription(description)
                    && isValidPrice(price) && isValidDuration(duration);
        } else {
            tagList = certificate.getTagList();
            tagListIsEmpty = tagList.stream().allMatch(tag -> tag.getId() > 1 && StringUtils.isNotEmpty(tag.getName()));
        }


        return !tagListIsEmpty && !areParamsEmpty && isValidName(name) && isValidDescription(description)
                && isValidPrice(price) && isValidDuration(duration) && isTagListValid(tagList);
    }

    public boolean areParamsEmpty(String... params) {
        return Arrays.stream(params).allMatch(String::isEmpty);
    }

    public void validateSearchCriteriaEmpty(SearchCriteria searchCriteria) {
        if (searchCriteria.getTagList() != null
                && StringUtils.isNotEmpty(searchCriteria.getCertificateName())
                && StringUtils.isNotEmpty(searchCriteria.getCertificateDescription())
                &&!StringUtils.isNotEmpty(searchCriteria.getSortByParameter())
                && searchCriteria.getOrderType() != null) {

            throw new InvalidEntityException(CertificateValidator.class);
        }
    }

    public void isParamsRegexValid(String name, String description, BigDecimal price, Duration duration) {
        boolean allValid = true;
        if (StringUtils.isNotEmpty(name)) {
            allValid = isValidName(name);
        }
        if (StringUtils.isNotEmpty(description)) {
            allValid = isValidDescription(description);
        }
        if (price != null) {
            allValid = isValidPrice(price);
        }
        if (duration != null) {
            allValid = isValidDuration(duration);
        }

        if (!allValid) {
            throw new InvalidEntityException(Certificate.class);
        }
    }

    public Certificate checkForUpdatedFields(CertificateDto certificate, Optional<Certificate> certificateById) {
        if (certificateById.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Certificate temp = new Certificate();
        temp.setId(certificateById.get().getId());
        temp.setName(certificateById.get().getName());
        temp.setDescription(certificateById.get().getDescription());
        temp.setPrice(certificateById.get().getPrice());
        temp.setDuration(certificateById.get().getDuration());

        if (StringUtils.isNotEmpty(certificate.getName()) && !certificateById.get().getName().equals(certificate.getName())) {
            temp.setName(certificate.getName());
        }
        if (StringUtils.isNotEmpty(certificate.getDescription())
                && !certificate.getDescription().equals(certificateById.get().getDescription())) {

            temp.setDescription(certificate.getDescription());
        }
        if (certificate.getPrice() != null && !Objects.equals(certificate.getPrice(), certificateById.get().getPrice())) {
            temp.setPrice(certificate.getPrice());
        }
        if (certificate.getDuration() != null && !Objects.equals(certificate.getDuration(), certificateById.get().getDuration())) {
            temp.setDuration(certificate.getDuration());
        }

        return temp;
    }

    public boolean areAllParametersTheSame(Optional<Certificate> opCertificate, Certificate certificate) {
        if (opCertificate.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return StringUtils.equalsIgnoreCase(opCertificate.get().getName(), certificate.getName())
                && StringUtils.equalsIgnoreCase(opCertificate.get().getDescription(), certificate.getDescription())
                && Objects.equals(opCertificate.get().getPrice(), certificate.getPrice())
                && opCertificate.get().getDuration() == certificate.getDuration();
    }

    public void validateId(Long... id) {
        if (id == null || id.length < 1) {
            throw new InvalidEntityException(CertificateValidator.class);
        }
        Arrays.stream(id).forEach(i -> {
            String valueOfId = String.valueOf(i);
            if (!valueOfId.matches(VALID_ID_REGEX) && i < 1) {
                throw new InvalidEntityException(CertificateValidator.class);
            }
        });
    }

    private boolean isTagListValid(List<Tag> tagList) {
        return tagList.stream().allMatch(tagName -> Pattern.matches(TAG_NAME_REGEX, tagName.getName()));
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
