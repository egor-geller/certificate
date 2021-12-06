package com.epam.esm.dto.mapper;

import com.epam.esm.dto.SavedOrderDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.SavedOrder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SavedOrderServiceMapper {

    private SavedOrderServiceMapper() {
    }

    public SavedOrderDto convertSavedOrderToDto(SavedOrder savedOrder) {
        Long id = savedOrder.getId();
        Order order = savedOrder.getOrder();
        Certificate certificate = savedOrder.getCertificate();
        BigDecimal certificateCost = savedOrder.getCertificateCost();
        return new SavedOrderDto(id, order, certificate, certificateCost);
    }

    public SavedOrder convertSavedOrderFromDto(SavedOrderDto savedOrderDto) {
        SavedOrder savedOrder = new SavedOrder();
        savedOrder.setId(savedOrderDto.getId());
        savedOrder.setOrder(savedOrderDto.getOrder());
        savedOrder.setCertificate(savedOrderDto.getCertificate());
        savedOrder.setCertificateCost(savedOrderDto.getCost());
        return savedOrder;
    }
}
