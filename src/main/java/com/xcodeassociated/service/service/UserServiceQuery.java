package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserServiceQuery {
    Page<UserDto> getAllUsers(Pageable pageable);
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String emailQuery);
    UserDto getUserByFirstNameAndLastNameAndNullableEmail(String firstName, String lastName, String email);
}
