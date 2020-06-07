package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.controller.kafka.dto.KeycloakAdminEvent;
import com.xcodeassociated.service.controller.kafka.dto.KeycloakBaseEvent;
import com.xcodeassociated.service.controller.kafka.dto.KeycloakEvent;
import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.service.implementation.transition.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
class UserEventExecutor <T extends BaseUserEvent> {
    private T event;
    private Consumer<T> handler;

    public void execute() {
        this.handler.accept(this.event);
    }
}

@Slf4j
public class UserEventConverter {

    private final UserEventHandler userEventHandler;
    private final Map<String, Function<? super KeycloakBaseEvent, UserEventExecutor<? extends BaseUserEvent>>> matcher;

    private static final String DEFAULT_MISSING_DETAIL = "UNKNOWN";
    private static final String RESOURCE_PATH_SEPARATOR = "/";

    public UserEventConverter(UserEventHandler userEventHandler) {
        this.userEventHandler = userEventHandler;

        this.matcher = new HashMap<>();
        this.matcher.put("LOGIN", this::toLoginEvent);
        this.matcher.put("LOGOUT", this::toLogout);
        this.matcher.put("REGISTER", this::toRegister);
        this.matcher.put("UPDATE", this::toUpdate);
        this.matcher.put("DELETE", this::toDelete);
    }

    // operations: LOGIN, LOGOUT, REGISTER
    public void dispatch(KeycloakEvent event) {
        String operation = event.getType().getFormatted();
        Optional.ofNullable(this.matcher.get(operation))
                .ifPresentOrElse(e -> e.apply(event).execute(),
                        () -> log.info("Could not find converter function for operation type: {}", operation));

    }

    // operations: DELETE, UPDATE
    public void dispatch(KeycloakAdminEvent event) {
        String operation = event.getOperationType().getFormatted();
        Optional.ofNullable(this.matcher.get(operation))
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, "Could not get user event converter function for: %s", operation))
                .apply(event)
                .execute();
    }

    private UserEventExecutor<Login> toLoginEvent(KeycloakBaseEvent event) {
        return Stream.of((KeycloakEvent)event)
                .map(e -> new Login().toBuilder()
                        .eventType(EventType.LOGIN)
                        .time(e.getTime())
                        .realmId(e.getRealmId())
                        .sessionId(e.getSessionId())
                        .error(e.getError())
                        .userId(e.getUserId())
                        .build())
                .map(e -> new UserEventExecutor<Login>()
                        .toBuilder()
                        .event(e)
                        .handler(this.userEventHandler::handleLogin)
                        .build())
                .findFirst()
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, "Could not construct user event executor: LOGIN"));
    }

    private UserEventExecutor<Logout> toLogout(KeycloakBaseEvent event) {
        return Stream.of((KeycloakEvent)event)
                .map(e -> new Logout().toBuilder()
                        .eventType(EventType.LOGOUT)
                        .time(e.getTime())
                        .realmId(e.getRealmId())
                        .sessionId(e.getSessionId())
                        .error(e.getError())
                        .userId(e.getUserId())
                        .build())
                .map(e -> new UserEventExecutor<Logout>()
                        .toBuilder()
                        .event(e)
                        .handler(this.userEventHandler::handleLogout)
                        .build())
                .findFirst()
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, "Could not construct user event executor: LOGOUT"));
    }

    private UserEventExecutor<Register> toRegister(KeycloakBaseEvent event) {
        return Stream.of((KeycloakEvent)event)
                .map(e -> new Register().toBuilder()
                        .eventType(EventType.REGISTER)
                        .time(e.getTime())
                        .realmId(e.getRealmId())
                        .error(e.getError())
                        .userId(e.getUserId())
                        .email(e.getDetails().getOrDefault("email", DEFAULT_MISSING_DETAIL))
                        .username(e.getDetails().getOrDefault("username", DEFAULT_MISSING_DETAIL))
                        .build())
                .map(e -> new UserEventExecutor<Register>()
                        .toBuilder()
                        .event(e)
                        .handler(this.userEventHandler::handleRegister)
                        .build())
                .findFirst()
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, "Could not construct user event executor: REGISTER"));
    }

    private UserEventExecutor<Update> toUpdate(KeycloakBaseEvent event) {
        return Stream.of((KeycloakAdminEvent)event)
                .map(e -> new Update().toBuilder()
                        .eventType(EventType.UPDATE)
                        .time(e.getTime())
                        .realmId(e.getRealmId())
                        .error(e.getError())
                        .userId(this.resourcePathToUserId(e.getResourcePath()))
                        .build())
                .map(e -> new UserEventExecutor<Update>()
                        .toBuilder()
                        .event(e)
                        .handler(this.userEventHandler::handleUpdate)
                        .build())
                .findFirst()
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, "Could not construct user event executor: UPDATE"));
    }

    private UserEventExecutor<Delete> toDelete(KeycloakBaseEvent event) {
        return Stream.of((KeycloakAdminEvent)event)
                .map(e -> new Delete().toBuilder()
                        .eventType(EventType.DELETE)
                        .time(e.getTime())
                        .realmId(e.getRealmId())
                        .error(e.getError())
                        .userId(this.resourcePathToUserId(e.getResourcePath()))
                        .build())
                .map(e -> new UserEventExecutor<Delete>()
                        .toBuilder()
                        .event(e)
                        .handler(this.userEventHandler::handleDelete)
                        .build())
                .findFirst()
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, "Could not construct user event executor: DELETE"));
    }

    private String resourcePathToUserId(String resourcePath) {
        return Optional.ofNullable(List.of(resourcePath.split(RESOURCE_PATH_SEPARATOR)).get(1))
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, "Could not parse resource path"));
    }
}
