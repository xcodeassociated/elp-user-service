package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserDto;
import com.xcodeassociated.service.model.dto.UserFullDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserServiceQuery {
    Page<UserDto> getAllUsers(Pageable pageable);
    UserDto getUserById(Long id);
    UserFullDto getFullUserById(Long id);
    UserDto getUserByAuthId(String id);
    UserDto getUserByEmail(String emailQuery);
    UserDto getUserByFirstNameAndLastNameAndNullableEmail(String firstName, String lastName, String email);
}
