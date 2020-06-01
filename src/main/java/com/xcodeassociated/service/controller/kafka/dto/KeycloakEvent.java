package com.xcodeassociated.service.controller.kafka.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class KeycloakEvent extends KeycloakBaseEvent {
    private String userId;
    private String clientId;
    private String sessionId;
    private String ipAddress;
    private String error;
    private Map<String, String> details;

    public static KeycloakEvent formKafkaMessage(KeycloakBaseEvent event) {
        return new KeycloakEvent()
                .toBuilder()
                .time(event.getTime())
                .realmId(event.getRealmId())
                .unknownFields(Map.of())
                .type(event.getType())
                .userId((String)event.getUnknownFields().get("userId"))
                .clientId((String)event.getUnknownFields().get("clientId"))
                .sessionId((String)event.getUnknownFields().get("sessionId"))
                .ipAddress((String)event.getUnknownFields().get("ipAddress"))
                .error((String)event.getUnknownFields().get("error"))
                .details((HashMap<String, String>) event.getUnknownFields().get("details"))
                .build();
    }
}