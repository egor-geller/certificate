package com.epam.esm.service.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.SavedOrderDto;
import com.epam.esm.dto.mapper.OrderServiceMapper;
import com.epam.esm.dto.mapper.SavedOrderServiceMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.SavedOrder;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EmptyOrderException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.impl.CertificateRepositoryImpl;
import com.epam.esm.repository.impl.OrderRepositoryImpl;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.SavedOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepositoryImpl orderRepository;
    private final UserRepositoryImpl userRepository;
    private final OrderServiceMapper orderServiceMapper;
    private final CertificateRepositoryImpl certificateRepository;
    private final SavedOrderService savedOrderService;
    private final SavedOrderServiceMapper savedOrderServiceMapper;

    @Autowired
    public OrderServiceImpl(OrderRepositoryImpl orderRepository,
                            UserRepositoryImpl userRepository,
                            OrderServiceMapper orderServiceMapper,
                            CertificateRepositoryImpl certificateRepository,
                            SavedOrderService savedOrderService,
                            SavedOrderServiceMapper savedOrderServiceMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderServiceMapper = orderServiceMapper;
        this.certificateRepository = certificateRepository;
        this.savedOrderService = savedOrderService;
        this.savedOrderServiceMapper = savedOrderServiceMapper;
    }

    @Override
    public List<OrderDto> findAllOrdersService(PaginationContext paginationContext) {
        return orderRepository.findAll(paginationContext)
                .stream()
                .map(orderServiceMapper::convertOrderToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto findOrderByIdService(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        return orderServiceMapper.convertOrderToDto(order);
    }

    @Override
    public List<OrderDto> findByOrderByUserService(long userId, PaginationContext paginationContext) {
        return orderRepository.findByUserId(paginationContext, userId)
                .stream()
                .map(orderServiceMapper::convertOrderToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto makeOrderService(OrderDto orderDto) {
        long userId = orderDto.getUserId();
        List<Certificate> certificateIdList = orderDto.getCertificateList();

        if (certificateIdList.isEmpty()) {
            throw new EmptyOrderException();
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(String.valueOf(userId)));
        List<Long> certificateIds = certificateIdList.stream().map(Certificate::getId).collect(Collectors.toList());
        Order preparedOrder = prepareOrder(certificateIds, user);
        Order createdOrder = orderRepository.create(preparedOrder);
        List<SavedOrder> savedOrders = certificateIdList
                .stream()
                .filter(certificate -> certificateRepository.findById(certificate.getId()).isPresent())
                .map(certificate -> {
                    SavedOrder savedOrder = new SavedOrder();
                    savedOrder.setCertificate(certificate);
                    savedOrder.setCertificateCost(certificate.getPrice());
                    return savedOrder;
                })
                .collect(Collectors.toList());
        savedOrders.forEach(savedOrder -> savedOrder.setOrder(createdOrder));
        savedOrders.forEach(savedOrder -> {
            SavedOrderDto savedOrderDto = savedOrderServiceMapper.convertSavedOrderToDto(savedOrder);
            savedOrderService.create(savedOrderDto);
        });

        return orderServiceMapper.convertOrderToDto(createdOrder);
    }

    @Override
    public Long count() {
        return orderRepository.count();
    }

    @Override
    public Long countByUser(Long id) {
        return orderRepository.countByUser(id);
    }

    private Order prepareOrder(List<Long> certificates, User user) {
        ZonedDateTime purchaseDate = ZonedDateTime.now();

        List<Certificate> certificateList = certificates
                .stream()
                .map(certificate -> {
                    Optional<Certificate> optionalCertificate = certificateRepository.findById(certificate);
                    if (optionalCertificate.isEmpty()) {
                        throw new EntityNotFoundException(String.valueOf(certificate));
                    }
                    Certificate cert = new Certificate();
                    cert.setId(optionalCertificate.get().getId());
                    cert.setName(optionalCertificate.get().getName());
                    cert.setDescription(optionalCertificate.get().getDescription());
                    cert.setDuration(optionalCertificate.get().getDuration());
                    cert.setPrice(optionalCertificate.get().getPrice());
                    cert.setCreateDate(optionalCertificate.get().getCreateDate());
                    cert.setLastUpdateDate(optionalCertificate.get().getLastUpdateDate());
                    return cert;
                })
                .collect(Collectors.toList());

        BigDecimal cost = certificateList.stream()
                .map(Certificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setPurchaseDate(purchaseDate);
        order.setCost(cost);
        order.setUser(user);
        order.setCertificateList(certificateList);

        return order;
    }
}
