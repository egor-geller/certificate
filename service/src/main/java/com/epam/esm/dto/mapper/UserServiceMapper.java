package com.epam.esm.dto.mapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserServiceMapper {

    private UserServiceMapper() {
    }

    public UserDto convertUserToDto(User user) {
        Long id = user.getId();
        String name = user.getName();
        return new UserDto(id, name);
    }

    public User convertUserFromDto(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        return user;
    }
}
