package com.xcodeassociated.service.service;

import com.xcodeassociated.events.model.KafkaEvent;
import com.xcodeassociated.service.controller.kafka.dto.KeycloakAdminEvent;
import com.xcodeassociated.service.controller.kafka.dto.KeycloakEvent;

public interface UserEventServiceInterface {
    void handleKafkaEvent(KafkaEvent event);
    void handleKeycloakEvent(KeycloakEvent event);
    void handleKeycloakAdminEvent(KeycloakAdminEvent event);
}
