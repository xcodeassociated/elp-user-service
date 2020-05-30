package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserDto;
import com.xcodeassociated.service.model.dto.simple.UserSimpleEmailsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface UserServiceQuery {
    Page<UserDto> getAllUsers(Pageable pageable);
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String emailQuery);
    UserDto getUserByFirstNameAndLastNameAndNullableEmail(String firstName, String lastName, String email);
    Page<UserSimpleEmailsDto> getAllUsersEmailsById(Set<Long> ids, Pageable pageable);
}
