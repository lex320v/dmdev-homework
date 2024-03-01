package com.example.service;

import com.example.dao.UserRepository;
import com.example.dto.UserCreateDto;
import com.example.dto.UserReadDto;
import com.example.entity.User;
import com.example.mapper.Mapper;
import com.example.mapper.UserCreateMapper;
import com.example.mapper.UserReadMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;

    @Transactional
    public Long create(UserCreateDto userDto) {
        var user = userCreateMapper.mapFrom(userDto);
        return userRepository.save(user).getId();
    }

    @Transactional
    public Optional<UserReadDto> findById(Long id) {
        return findById(id, userReadMapper);
    }

    @Transactional
    public <T> Optional<T> findById(Long id, Mapper<User, T> mapper) {
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJakartaHintName(),
                userRepository.getEntityManager().getEntityGraph("withPersonalInfo")
        );
        return userRepository.findById(id, properties).map(mapper::mapFrom);
    }

    @Transactional
    public boolean delete(Long id) {
        var optionalUser = userRepository.findById(id);
        optionalUser.ifPresent(user -> userRepository.delete(user.getId()));
        return optionalUser.isPresent();
    }
}
