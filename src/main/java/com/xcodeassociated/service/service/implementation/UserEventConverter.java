package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.controller.kafka.dto.KeycloakAdminEvent;
import com.xcodeassociated.service.controller.kafka.dto.KeycloakEvent;
import com.xcodeassociated.service.service.implementation.transition.BaseUserEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class UserEventConverter {

    public BaseUserEvent convertFromDto(KeycloakEvent event) {
        return null;
    }

    public BaseUserEvent convertFromDto(KeycloakAdminEvent event) {
        return null;
    }
}
