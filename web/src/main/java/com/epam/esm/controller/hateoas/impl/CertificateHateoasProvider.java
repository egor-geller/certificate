package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.hateoas.ModelHateoasProvider;
import com.epam.esm.dto.CertificateDto;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.controller.hateoas.impl.ResourceRelName.ALL_CERTIFICATES_REL;

@Component
public class CertificateHateoasProvider extends ModelHateoasProvider<CertificateDto> {

    @Override
    protected List<Link> addSpecificLinks(List<Link> baseLinks, CertificateDto model) {
        return baseLinks;
    }

    @Override
    protected Class<?> getControllerClass() {
        return CertificateController.class;
    }

    @Override
    protected String getAllResourcesRel() {
        return ALL_CERTIFICATES_REL;
    }
}
