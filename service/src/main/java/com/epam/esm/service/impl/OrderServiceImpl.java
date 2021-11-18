package com.epam.esm.service.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.mapper.OrderServiceMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EmptyOrderException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.impl.CertificateRepositoryImpl;
import com.epam.esm.repository.impl.OrderRepositoryImpl;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepositoryImpl orderRepository;
    private final UserRepositoryImpl userRepository;
    private final OrderServiceMapper orderServiceMapper;
    private final CertificateRepositoryImpl certificateRepository;

    @Autowired
    public OrderServiceImpl(OrderRepositoryImpl orderRepository,
                            UserRepositoryImpl userRepository,
                            OrderServiceMapper orderServiceMapper,
                            CertificateRepositoryImpl certificateRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderServiceMapper = orderServiceMapper;
        this.certificateRepository = certificateRepository;
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
        Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
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
        List<Long> certificateIdList = orderDto.getCertificateList();

        if (certificateIdList.isEmpty()) {
            throw new EmptyOrderException();
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userId));

        Order preparedOrder = prepareOrder(certificateIdList, user);
        Order createdOrder = orderRepository.create(preparedOrder);
        return orderServiceMapper.convertOrderToDto(createdOrder);
    }

    @Override
    public Long count() {
        return orderRepository.count();
    }

    @Override
    public Long countByUser() {
        return orderRepository.countByUser();
    }

    private Order prepareOrder(List<Long> certificates, User user) {
        ZonedDateTime purchaseDate = ZonedDateTime.now();

        List<Certificate> certificateList = certificates
                .stream()
                .map(certificateRepository::findById)
                .map(certificate -> {
                    if (certificate.isEmpty()) {
                        throw new EntityNotFoundException();
                    }
                    Certificate cert = new Certificate();
                    cert.setId(certificate.get().getId());
                    cert.setName(certificate.get().getName());
                    cert.setDescription(certificate.get().getDescription());
                    cert.setDuration(certificate.get().getDuration());
                    cert.setPrice(certificate.get().getPrice());
                    cert.setCreateDate(certificate.get().getCreateDate());
                    cert.setLastUpdateDate(certificate.get().getLastUpdateDate());
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
