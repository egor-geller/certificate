package com.epam.esm.dto.mapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class UserServiceMapper {

    private static final Logger logger = LogManager.getLogger();

    private UserServiceMapper() {
    }

    public UserDto convertUserToDto(User user) {
        Long id = user.getId();
        String username = user.getUsername();
        String password = user.getPassword();
        Role role = user.getRole();
        return new UserDto(id, username, password, role);
    }

    public User convertUserFromDto(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        logger.info("User after mapping is {}", user);
        return user;
    }
}
