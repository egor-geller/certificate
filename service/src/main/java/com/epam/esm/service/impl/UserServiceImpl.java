package com.epam.esm.service.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapper.UserServiceMapper;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.repositoryinterfaces.UserRepository;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserServiceMapper userServiceMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserServiceMapper userServiceMapper) {
        this.userRepository = userRepository;
        this.userServiceMapper = userServiceMapper;
    }

    @Override
    public List<UserDto> findAllUsers(PaginationContext paginationContext) {
        return userRepository.findAll(paginationContext)
                .stream()
                .map(userServiceMapper::convertUserToDto)
                .collect(Collectors.toList());

    }

    @Override
    public UserDto findUserByIdService(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        return userServiceMapper.convertUserToDto(user);
    }

    @Override
    public Long count(){
        return userRepository.count();
    }
}
