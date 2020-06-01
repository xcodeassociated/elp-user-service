package com.xcodeassociated.service.controller.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xcodeassociated.events.model.KafkaEvent;
import com.xcodeassociated.service.controller.kafka.dto.KeycloakBaseEvent;

public interface KafkaConsumerInterface {
    void onKeycloakEvent(KeycloakBaseEvent event, Integer partition, Integer offset) throws JsonProcessingException;
    void onDataEvent(KafkaEvent event, Integer partition, Integer offset);
}
