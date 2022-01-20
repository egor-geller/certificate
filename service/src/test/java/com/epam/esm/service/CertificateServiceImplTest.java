package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.mapper.CertificateServiceMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.builder.SortType;
import com.epam.esm.repository.impl.CertificateRepositoryImpl;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.impl.CertificateServiceImpl;
import com.epam.esm.validator.CertificateValidator;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

    private static final ZonedDateTime l = LocalDateTime.now().atZone(ZoneOffset.UTC);
    private static final int expectedInteractions = 1;

    @InjectMocks
    private CertificateServiceImpl certificateService;

    @Mock
    private CertificateRepositoryImpl certificateRepository;

    @Mock
    private TagRepositoryImpl tagRepository;

    @Mock
    private CertificateValidator certificateValidator;

    @Spy
    private TagValidator tagValidator;

    @Spy
    private CertificateServiceMapper certificateMapper;

    @Mock
    private PaginationContext paginationContext;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(CertificateServiceImplTest.class);
    }

    @Test
    void findByCriteriaTest() {
        List<Certificate> certificateList = new ArrayList<>();
        List<CertificateDto> certificateDtoList = new ArrayList<>();
        certificateList.add(createCertificate());
        certificateDtoList.add(createCertificateDto());

        when(certificateRepository.find(paginationContext.createPagination(1,10), provideSearchParameters())).thenReturn(certificateList);
        when(tagRepository.findByCertificateId(anyLong())).thenReturn(createTagList());

        List<CertificateDto> actualDtoList = certificateService
                .findCertificateByCriteria(paginationContext.createPagination(1,10), provideSearchParameters());


        int expectedInteractions = 1;
        verify(certificateRepository, times(expectedInteractions)).find(paginationContext.createPagination(1,10), provideSearchParameters());

        assertEquals(certificateDtoList.toString(), actualDtoList.toString());
    }

    @Test
    void findCertificateByIdTest() {
        long certificateId = 1;
        Certificate certificate = createCertificate();
        certificate.setId(certificateId);

        CertificateDto expectedCertificateDto = createCertificateDto();
        expectedCertificateDto.setId(certificateId);

        when(certificateRepository.findById(1L)).thenReturn(Optional.of(certificate));
        when(tagRepository.findByCertificateId(1L)).thenReturn(createTagList());

        CertificateDto actualCertificateDto = certificateService.findCertificateById(certificateId);

        verify(certificateRepository, times(expectedInteractions)).findById(1L);
        verify(tagRepository, times(expectedInteractions)).findByCertificateId(1L);

        Assertions.assertEquals(expectedCertificateDto.toString(), actualCertificateDto.toString());
    }

    @Test
    void findByIdWhenCertificateNotFoundTest() {
        int certificateId = 1;
        when(certificateRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> certificateService.findCertificateById(certificateId));
    }

    /*@Test
    @Transactional
    void createCertificateTest() {
        CertificateDto certificateDto = createCertificateDto();

        when(certificateValidator.isCertificateDtoValid(certificateDto)).thenReturn(true);

        certificateService.create(paginationContext.createPagination(1,10), certificateDto);

        CertificateDto certificateById = certificateService.findCertificateById(certificateDto.getId());

        //verify(certificateRepository, times(expectedInteractions)).create(any());
        Assertions.assertNotNull(certificateById);
    }*/

    @Test
    @Transactional
    void createInvalidCertificateTest() {
        CertificateDto certificateDto = null;

        Assertions.assertThrows(EntityNotFoundException.class, () -> certificateService.create(paginationContext.createPagination(1,10), certificateDto));
    }

    /*@Test
    void createAlreadyExistsCertificateTest() {
        CertificateDto certificateDto = createCertificateDto();
        Certificate certificate = createCertificate();
        certificate.setId(0L);
        certificateDto.setId(0L);
        when(certificateRepository.findById(0L)).thenReturn(Optional.of(certificate));

        when(certificateValidator.isCertificateDtoValid(certificateDto)).thenReturn(true);

        Assertions.assertThrows(EntityAlreadyExistsException.class, () -> certificateService.create(paginationContext.createPagination(1,10), certificateDto));
    }*/

   /* @Test
    @Transactional
    void updateCertificateTest() {
        long certificateId = 0;
        Certificate certificate = createCertificate();
        CertificateDto updatedCertificateDto = createCertificateDto();
        updatedCertificateDto.setId(certificateId);

        when(certificateRepository.update(certificate)).thenReturn(certificate);
        when(certificateValidator.isCertificateDtoValid(updatedCertificateDto)).thenReturn(true);
        when(certificateService.update(updatedCertificateDto)).thenReturn(updatedCertificateDto);
        Assertions.assertNotNull(updatedCertificateDto);
    }*/

    @Test
    @Transactional
    void updateInvalidCertificateTest() {
        CertificateDto updatedCertificateDto = createCertificateDto();
        updatedCertificateDto.setName("");

        Assertions.assertThrows(EntityNotFoundException.class, () -> certificateService.update(updatedCertificateDto));
    }

    @Test
    void updateInvalidTagTest() {
        List<Tag> stringList = new ArrayList<>();
        CertificateDto updatedCertificateDto = createCertificateDto();
        Tag tag = new Tag();
        stringList.add(tag);
        updatedCertificateDto.setTagList(stringList);

        Assertions.assertThrows(EntityNotFoundException.class, () -> certificateService.update(updatedCertificateDto));
    }

    /*@Test
    void deleteCertificateTest() {
        when(certificateRepository.delete(createCertificate())).thenReturn(void);

        long certificateId = 1;
        certificateRepository.delete(certificateId);

        verify(certificateRepository, times(expectedInteractions)).delete(anyLong());
    }*/

    @Test
    void deleteNotFoundCertificateIdTest() {
        long certificateId = 0;
        Assertions.assertThrows(NullPointerException.class, () -> certificateService.delete(certificateId, paginationContext.createPagination(1,10)));
    }

    private Certificate createCertificate() {
        Certificate certificate = new Certificate();
        certificate.setId(0L);
        certificate.setName("certificate1");
        certificate.setDescription("description1");
        certificate.setPrice(BigDecimal.ONE);
        certificate.setDuration(Duration.ofDays(1));
        certificate.setCreateDate(l);
        certificate.setLastUpdateDate(l);

        return certificate;
    }

    private CertificateDto createCertificateDto() {
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setId(0L);
        certificateDto.setName("certificate1");
        certificateDto.setDescription("description1");
        certificateDto.setPrice(BigDecimal.ONE);
        certificateDto.setDuration(Duration.ofDays(1));
        certificateDto.setCreateDate(l);
        certificateDto.setLastUpdateDate(l);
        certificateDto.setTagList(createTagNameList());

        return certificateDto;
    }

    private SearchCriteria provideSearchParameters() {
        SearchCriteria searchParameters = new SearchCriteria();

        searchParameters.setTagList(List.of("name1"));
        searchParameters.setCertificateName("certificate1");
        searchParameters.setCertificateDescription("description1");
        searchParameters.setSortByParameter("name");
        searchParameters.setOrderType(SortType.ASC);

        return searchParameters;
    }

    private List<Tag> createTagList() {
        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag();

        tag.setId(0);
        tag.setName("tag1");

        tags.add(tag);

        return tags;
    }

    private List<Tag> createTagNameList() {
        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag();
        tag.setId(0);
        tag.setName("tag1");
        tags.add(tag);

        return tags;
    }
}
