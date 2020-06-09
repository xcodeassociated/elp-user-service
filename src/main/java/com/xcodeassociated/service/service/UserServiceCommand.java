package com.xcodeassociated.service.service;

import com.xcodeassociated.service.controller.rest.keycloak.dto.UserRepresentationDto;
import com.xcodeassociated.service.model.User;
import com.xcodeassociated.service.service.implementation.transition.Delete;
import com.xcodeassociated.service.service.implementation.transition.Login;
import com.xcodeassociated.service.service.implementation.transition.Logout;

import java.util.Optional;

public interface UserServiceCommand {

    Optional<User> createUser(UserRepresentationDto userRepresentationDto);
    Optional<User> updateUser(UserRepresentationDto userRepresentationDto);
    Optional<User> registerUserLogin(Login event);
    Optional<User> registerUserLogout(Logout event);
    void deleteUserById(Long id);
    void deleteUser(Delete event);
}
