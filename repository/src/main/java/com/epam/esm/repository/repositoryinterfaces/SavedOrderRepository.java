package com.epam.esm.repository.repositoryinterfaces;

import com.epam.esm.entity.SavedOrder;

import java.util.List;

public interface SavedOrderRepository extends Repository<SavedOrder> {

    List<SavedOrder> findByOrderId(Long id);

    List<SavedOrder> findByUserId(Long id);
}
