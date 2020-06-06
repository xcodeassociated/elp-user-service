package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.controller.kafka.dto.KeycloakAdminEvent;
import com.xcodeassociated.service.controller.kafka.dto.KeycloakBaseEvent;
import com.xcodeassociated.service.controller.kafka.dto.KeycloakEvent;
import com.xcodeassociated.service.service.implementation.transition.BaseUserEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class UserEventConverter {

    private Map<String, Function<KeycloakBaseEvent, BaseUserEvent>> matcher;

    public UserEventConverter() {
        this.matcher = new HashMap<>();
        this.matcher.put("LOGIN", this::toLoginEvent);
        this.matcher.put("LOGOUT", this::toLogout);
        this.matcher.put("REGISTER", this::toRegister);
        this.matcher.put("UPDATE", this::toUpdate);
        this.matcher.put("DELETE", this::toDelete);
    }

    public BaseUserEvent convertFromDto(KeycloakEvent event) {
        return null;
    }

    public BaseUserEvent convertFromDto(KeycloakAdminEvent event) {
        return null;
    }

    private BaseUserEvent toLoginEvent(KeycloakBaseEvent event) {
        return null;
    }

    private BaseUserEvent toLogout(KeycloakBaseEvent event) {
        return null;
    }

    private BaseUserEvent toRegister(KeycloakBaseEvent event) {
        return null;
    }

    private BaseUserEvent toUpdate(KeycloakBaseEvent event) {
        return null;
    }

    private BaseUserEvent toDelete(KeycloakBaseEvent event) {
        return null;
    }
}
