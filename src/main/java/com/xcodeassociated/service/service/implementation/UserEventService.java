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
import com.xcodeassociated.service.service.UserEventServiceInterface;
import com.xcodeassociated.service.service.implementation.transition.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class UserEventService implements UserEventServiceInterface, UserEventHandler {

    private final UserEventConverter eventConverter;
    private final KeycloakApi keycloakApi;

    public UserEventService(KeycloakApi keycloakApi) {
        this.keycloakApi = keycloakApi;
        this.eventConverter = new UserEventConverter(this);
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
        // ...
    }

    @Override
    public void handleLogout(Logout event) {
        log.info("Handling LOGOUT: {}", event);
        // ...
    }

    @Override
    public void handleRegister(Register event) {
        log.info("Handling REGISTER: {}", event);

        Optional<UserRepresentationDto> userRepresentation = this.fetchUserRepresentation(event.getUserId());
        userRepresentation.ifPresent(userRepresentationDto -> log.info("Received user representation: {}", userRepresentationDto));
    }

    @Override
    public void handleUpdate(Update event) {
        log.info("Handling UPDATE: {}", event);

        Optional<UserRepresentationDto> userRepresentation = this.fetchUserRepresentation(event.getUserId());
        userRepresentation.ifPresent(userRepresentationDto -> log.info("Received user representation: {}", userRepresentationDto));
    }

    @Override
    public void handleDelete(Delete event) {
        log.info("Handling DELETE: {}", event);

    }

    private Optional<UserRepresentationDto> fetchUserRepresentation(String userId) {
        try {
            return Optional.ofNullable(this.keycloakApi.getUserRepresentation(userId).execute().body());
        } catch (IOException exception) {
            log.error("Could not get user representation with error: {}", exception.getMessage());
            return Optional.empty();
        }
    }
}
