package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.hateoas.ListHateoasProvider;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.repository.PaginationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.epam.esm.controller.hateoas.impl.ResourceRelName.ALL_CERTIFICATES_REL;

@Component
public class CertificateListHateoasProvider extends ListHateoasProvider<CertificateDto> {

    @Autowired
    protected CertificateListHateoasProvider(PaginationContext paginationContext) {
        super(paginationContext);
    }

    @Override
    protected Class<?> getControllerClass() {
        return CertificateController.class;
    }

    @Override
    protected String getAllResourcesRel() {
        return ALL_CERTIFICATES_REL;
    }

    @Override
    protected int getCurrentPage(PaginationContext paginationContext) {
        return paginationContext.getStartPage();
    }

    @Override
    protected int getPageSize(PaginationContext paginationContext) {
        return paginationContext.getLengthOfContext();
    }
}
