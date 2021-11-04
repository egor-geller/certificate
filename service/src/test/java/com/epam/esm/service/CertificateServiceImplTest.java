package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.mapper.CertificateServiceMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
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

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        when(certificateRepository.find(provideSearchParameters())).thenReturn(certificateList);
        when(tagRepository.findByCertificateId(anyLong())).thenReturn(createTagList());

        List<CertificateDto> actualDtoList = certificateService
                .findCertificateByCriteria(provideSearchParameters());


        int expectedInteractions = 1;
        verify(certificateRepository, times(expectedInteractions)).find(provideSearchParameters());

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

    @Test
    void createCertificateTest() {
        CertificateDto certificateDto = createCertificateDto();

        when(certificateValidator.isCertificateDtoValid(certificateDto)).thenReturn(true);

        certificateService.create(certificateDto);

        verify(certificateRepository, times(expectedInteractions)).create(any());
    }

    @Test
    void createInvalidCertificateTest() {
        CertificateDto certificateDto = null;

        Assertions.assertThrows(InvalidEntityException.class, () -> certificateService.create(certificateDto));
    }

    @Test
    void createAlreadyExistsCertificateTest() {
        CertificateDto certificateDto = createCertificateDto();
        Certificate certificate = createCertificate();
        certificate.setId(0L);
        certificateDto.setId(0L);
        when(certificateRepository.findById(0L)).thenReturn(Optional.of(certificate));

        when(certificateValidator.isCertificateDtoValid(certificateDto)).thenReturn(true);

        Assertions.assertThrows(EntityAlreadyExistsException.class, () -> certificateService.create(certificateDto));
    }

    @Test
    void updateCertificateTest() {
        long certificateId = 0;
        Certificate certificate = createCertificate();
        CertificateDto updatedCertificateDto = createCertificateDto();
        updatedCertificateDto.setId(certificateId);

        when(certificateRepository.update(certificate)).thenReturn(true);
        when(certificateValidator.isCertificateDtoValid(updatedCertificateDto)).thenReturn(true);
        assertTrue(certificateService.update(updatedCertificateDto));
    }

    @Test
    void updateInvalidCertificateTest() {
        CertificateDto updatedCertificateDto = createCertificateDto();
        updatedCertificateDto.setName("");

        Assertions.assertThrows(InvalidEntityException.class, () -> certificateService.update(updatedCertificateDto));
    }

    @Test
    void updateInvalidTagTest() {
        List<String> stringList = new ArrayList<>();
        CertificateDto updatedCertificateDto = createCertificateDto();
        stringList.add("");
        updatedCertificateDto.setTagList(stringList);

        Assertions.assertThrows(InvalidEntityException.class, () -> certificateService.update(updatedCertificateDto));
    }

    @Test
    void deleteCertificateTest() {
        when(certificateRepository.delete(anyLong())).thenReturn(true);

        long certificateId = 1;
        certificateRepository.delete(certificateId);

        verify(certificateRepository, times(expectedInteractions)).delete(anyLong());
    }

    @Test
    void deleteNotFoundCertificateIdTest() {
        long certificateId = 0;
        Assertions.assertThrows(InvalidEntityException.class, () -> certificateService.delete(certificateId));
    }

    private Certificate createCertificate() {
        Certificate certificate = new Certificate();
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

        searchParameters.setTagName("tag1");
        searchParameters.setCertificateName("certificate1");
        searchParameters.setCertificateDescription("description1");
        searchParameters.setSortByName(SortType.ASC);
        searchParameters.setSortByCreateDate(SortType.ASC);

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

    private List<String> createTagNameList() {
        List<String> tags = new ArrayList<>();
        tags.add("tag1");

        return tags;
    }
}
