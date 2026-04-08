package by.lebenkov.task_tracker.api.mapper;

import by.lebenkov.task_tracker.storage.dto.userDto.UserRequest;
import by.lebenkov.task_tracker.storage.dto.userDto.UserResponse;
import by.lebenkov.task_tracker.storage.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(request.getPassword()))")
    @Mapping(target = "role", constant = "ROLE_USER")
    public abstract User toEntity(UserRequest request);

    public abstract UserResponse toResponse(User user);
}
