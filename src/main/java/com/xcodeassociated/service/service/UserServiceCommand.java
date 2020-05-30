package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserDto;

public interface UserServiceCommand {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    void deleteUser(Long id);
}
