package com.epam.esm.dto.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Component
public class OrderServiceMapper {

    private OrderServiceMapper() {
    }

    public OrderDto convertOrderToDto(Order order) {
        Long id = order.getId();
        Long userId = order.getUser().getId();
        BigDecimal orderCost = order.getCost();
        ZonedDateTime purchaseDate = order.getPurchaseDate();

        return new OrderDto(id, userId, order.getCertificateList(), orderCost, purchaseDate);
    }
}
