package com.epam.esm.validator;

import com.epam.esm.dto.OrderDto;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionValidator {

    public boolean check(Long id, List<OrderDto> someObject) {
        boolean b = someObject.stream().anyMatch(s -> s.getUserId().equals(id));
        if (!b) {
            throw new AccessDeniedException("Access denied");
        }
        return true;
    }
}
