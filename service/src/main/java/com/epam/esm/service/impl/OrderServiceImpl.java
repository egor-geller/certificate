package com.epam.esm.service.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.mapper.OrderServiceMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EmptyOrderException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.PaginationContext;
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

    @Autowired
    public OrderServiceImpl(OrderRepositoryImpl orderRepository,
                            UserRepositoryImpl userRepository,
                            OrderServiceMapper orderServiceMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderServiceMapper = orderServiceMapper;
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
        List<Certificate> certificateList = orderDto.getCertificateList();

        if (certificateList.isEmpty()) {
            throw new EmptyOrderException();
        }

        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        Order preparedOrder = prepareOrder(certificateList, user);
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

    private Order prepareOrder(List<Certificate> certificates, User user) {
        ZonedDateTime purchaseDate = ZonedDateTime.now();
        BigDecimal cost = certificates.stream()
                .map(Certificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setPurchaseDate(purchaseDate);
        order.setCost(cost);
        order.setUser(user);
        order.setCertificateList(certificates);

        return order;
    }
}
