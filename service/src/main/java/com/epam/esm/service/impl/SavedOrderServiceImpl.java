package com.epam.esm.service.impl;

import com.epam.esm.dto.SavedOrderDto;
import com.epam.esm.dto.mapper.SavedOrderServiceMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.SavedOrder;
import com.epam.esm.exception.EmptyOrderException;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.impl.SavedOrderRepositoryImpl;
import com.epam.esm.service.SavedOrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SavedOrderServiceImpl implements SavedOrderService {

    private static final Logger logger = LogManager.getLogger();

    private final SavedOrderRepositoryImpl savedOrderRepository;
    private final SavedOrderServiceMapper savedOrderServiceMapper;

    @Autowired
    public SavedOrderServiceImpl(SavedOrderRepositoryImpl savedOrderRepository,
                                 SavedOrderServiceMapper savedOrderServiceMapper) {
        this.savedOrderRepository = savedOrderRepository;
        this.savedOrderServiceMapper = savedOrderServiceMapper;
    }

    @Override
    public List<SavedOrderDto> findAll(PaginationContext paginationContext) {
        return savedOrderRepository.findAll(paginationContext)
                .stream()
                .map(savedOrder -> {
                    SavedOrder so = new SavedOrder();
                    so.setId(savedOrder.getId());
                    so.setOrder(savedOrder.getOrder());
                    Certificate certificate = savedOrder.getCertificate();
                    certificate.setPrice(savedOrder.getCertificateCost());
                    so.setCertificate(certificate);
                    so.setCertificateCost(savedOrder.getCertificateCost());
                    return so;
                })
                .map(savedOrderServiceMapper::convertSavedOrderToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SavedOrderDto findById(Long id) {
        SavedOrder savedOrder = savedOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        return savedOrderServiceMapper.convertSavedOrderToDto(savedOrder);
    }

    @Override
    public List<SavedOrderDto> findByOrderId(Long id) {
        List<SavedOrder> savedOrders = savedOrderRepository.findByOrderId(id);
        return savedOrders
                .stream()
                .map(savedOrder -> {
                    SavedOrder so = new SavedOrder();
                    so.setId(savedOrder.getId());
                    so.setOrder(savedOrder.getOrder());
                    Certificate certificate = savedOrder.getCertificate();
                    certificate.setPrice(savedOrder.getCertificateCost());
                    so.setCertificate(certificate);
                    so.setCertificateCost(savedOrder.getCertificateCost());
                    return so;
                })
                .map(savedOrderServiceMapper::convertSavedOrderToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SavedOrderDto> findByUserId(Long id) {
        List<SavedOrder> savedOrders = savedOrderRepository.findByUserId(id);
        return savedOrders
                .stream()
                .map(savedOrder -> {
                    SavedOrder so = new SavedOrder();
                    so.setId(savedOrder.getId());
                    so.setOrder(savedOrder.getOrder());
                    Certificate certificate = savedOrder.getCertificate();
                    certificate.setPrice(savedOrder.getCertificateCost());
                    so.setCertificate(certificate);
                    so.setCertificateCost(savedOrder.getCertificateCost());
                    return so;
                })
                .map(savedOrderServiceMapper::convertSavedOrderToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SavedOrderDto create(SavedOrderDto savedOrderDto) {
        if (savedOrderDto == null) {
            throw new EmptyOrderException();
        }
        SavedOrder savedOrder = savedOrderServiceMapper.convertSavedOrderFromDto(savedOrderDto);
        logger.info("SavedOrderService - savedOrder: {}", savedOrder);
        long id = savedOrder.getId() == null ? 0L : savedOrder.getId();
        Optional<SavedOrder> savedOrderId = savedOrderRepository.findById(id);
        logger.info("SavedOrderService - savedOrderId: {}", savedOrderId);
        if (savedOrderId.isPresent()) {
            throw new EntityAlreadyExistsException();
        }
        SavedOrder newSavedOrder = savedOrderRepository.create(savedOrder);
        logger.info("SavedOrderService - new saved order: {}", newSavedOrder);
        SavedOrderDto savedOrderDto1 = savedOrderServiceMapper.convertSavedOrderToDto(newSavedOrder);
        logger.info("SavedOrderService - new DTO: {}", savedOrderDto1);
        return savedOrderDto1;
    }

    @Override
    public Long count() {
        return savedOrderRepository.count();
    }
}
