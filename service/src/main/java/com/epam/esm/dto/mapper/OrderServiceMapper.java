package com.epam.esm.dto.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class OrderServiceMapper {

    private OrderServiceMapper() {
    }

    public OrderDto convertOrderToDto(Order order) {
        Long id = order.getId();
        Long userId = order.getUser().getId();
        BigDecimal orderCost = order.getCost();
        ZonedDateTime purchaseDate = order.getPurchaseDate();
        List<Certificate> certificateIds = order.getCertificateList();

        return new OrderDto(id, userId, certificateIds, orderCost, purchaseDate);
    }

    public Order convertOrderFromDto(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setUser(order.getUser());
        order.setCost(orderDto.getCost());
        order.setPurchaseDate(orderDto.getPurchaseDate());
        order.setCertificateList(orderDto.getCertificateList());

        return order;
    }
}
