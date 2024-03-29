package com.xcodeassociated.service.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcodeassociated.events.model.KafkaEvent;
import com.xcodeassociated.events.model.KafkaEventType;
import com.xcodeassociated.events.model.v1.SampleDataEventV1;
import com.xcodeassociated.service.controller.kafka.dto.KeycloakAdminEvent;
import com.xcodeassociated.service.controller.kafka.dto.KeycloakEvent;
import com.xcodeassociated.service.controller.rest.keycloak.KeycloakApi;
import com.xcodeassociated.service.controller.rest.keycloak.dto.UserRepresentationDto;
import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.service.UserEventServiceInterface;
import com.xcodeassociated.service.service.implementation.transition.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class UserEventService implements UserEventServiceInterface, UserEventHandler {

    private static final int KEYCLOAK_FETCH_DELAY_MS = 1000;

    private final UserEventConverter eventConverter;
    private final UserService userService;
    private final KeycloakApi keycloakApi;

    public UserEventService(UserService userService, KeycloakApi keycloakApi) {
        this.userService = userService;
        this.eventConverter = new UserEventConverter(this);
        this.keycloakApi = keycloakApi;
    }

    @Override
    public void handleKafkaEvent(KafkaEvent event) {
        log.info("Processing KafkaEvent: {}", event);
        if (event.getType().equals(KafkaEventType.SAMPLE_DATA) && event.getVersion().equals(1)) {
            SampleDataEventV1 sampleData = (SampleDataEventV1) event.getData();
            log.info("Received SAMPLE_DATA v1 event with payload: {}", sampleData.getPayload());
        }

        printFormattedEvent(event);
    }

    @Override
    public void handleKeycloakEvent(KeycloakEvent event) {
        log.info("Processing KeycloakEvent: {}", event);
        eventConverter.dispatch(event);
    }

    @Override
    public void handleKeycloakAdminEvent(KeycloakAdminEvent event) {
        log.info("Processing KeycloakAdminEvent: {}", event);
        eventConverter.dispatch(event);
    }

    private void printFormattedEvent(KafkaEvent event) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.debug("Event formatted message: {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleLogin(Login event) {
        log.info("Handling LOGIN: {}", event);

        this.userService.registerUserLogin(event)
                .ifPresent(e -> log.info("Event processing finished"));
    }

    @Override
    public void handleLogout(Logout event) {
        log.info("Handling LOGOUT: {}", event);

        this.userService.registerUserLogout(event)
                .ifPresent(e -> log.info("Event processing finished"));
    }

    @Override
    public void handleRegister(Register event) {
        log.info("Handling REGISTER: {}", event);

        Optional<UserRepresentationDto> userRepresentation = this.fetchUserRepresentation(event.getUserId());
        userRepresentation.ifPresentOrElse(userRepresentationDto -> {
            log.info("Received user representation: {}", userRepresentationDto);
            this.userService.createUser(userRepresentationDto)
                    .ifPresent(e -> log.info("Event processing finished"));
        }, () -> {
            log.error("Could not fetch user representation");
            throw new ServiceException(ErrorCode.A000, "Could not fetch keycloak api");
        });
    }

    @Override
    public void handleUpdate(Update event) {
        log.info("Handling UPDATE: {}", event);

        Optional<UserRepresentationDto> userRepresentation = this.fetchUserRepresentation(event.getUserId());
        userRepresentation.ifPresentOrElse(userRepresentationDto -> {
            log.info("Received user representation: {}", userRepresentationDto);
            this.userService.updateUser(userRepresentationDto)
                    .ifPresent(e -> log.info("Event processing finished"));
        }, () -> {
            log.error("Could not fetch user representation");
            throw new ServiceException(ErrorCode.A000, "Could not fetch keycloak api");
        });
    }

    @Override
    public void handleDelete(Delete event) {
        log.info("Handling DELETE: {}", event);

        this.userService.deleteUser(event);
        log.info("Event processing finished");
    }

    private Optional<UserRepresentationDto> fetchUserRepresentation(String userId) {
        try {
            // note: work-around keycloak lag when user updated the representation may not be up-to-date right away
            log.info("Thread enters {}ms sleep to prevent dirty representation fetch", KEYCLOAK_FETCH_DELAY_MS);
            Thread.sleep(KEYCLOAK_FETCH_DELAY_MS);
            log.info("Thread resumes after {}ms sleep", KEYCLOAK_FETCH_DELAY_MS);

            return Optional.ofNullable(this.keycloakApi.getUserRepresentation(userId).execute().body());
        } catch (IOException exception) {
            log.error("Could not get user representation with error: {}", exception.getMessage());
            return Optional.empty();
        } catch (InterruptedException exception) {
            log.error("InterruptedException: {}", exception.getMessage());
            return Optional.empty();
        }
    }
}
