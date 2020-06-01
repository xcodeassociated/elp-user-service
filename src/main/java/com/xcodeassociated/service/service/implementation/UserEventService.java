package com.xcodeassociated.service.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcodeassociated.events.model.KafkaEvent;
import com.xcodeassociated.events.model.KafkaEventType;
import com.xcodeassociated.events.model.v1.SampleDataEventV1;
import com.xcodeassociated.service.controller.kafka.dto.KeycloakAdminEvent;
import com.xcodeassociated.service.controller.kafka.dto.KeycloakEvent;
import com.xcodeassociated.service.service.UserEventServiceInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserEventService implements UserEventServiceInterface {

    private UserEventConverter eventConverter;

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
    }

    @Override
    public void handleKeycloakAdminEvent(KeycloakAdminEvent event) {
        log.info("Processing KeycloakAdminEvent: {}", event);
    }

    private void printFormattedEvent(KafkaEvent event) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.debug("Event formatted message: {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
