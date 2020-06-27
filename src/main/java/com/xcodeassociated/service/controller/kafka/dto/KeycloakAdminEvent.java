package com.xcodeassociated.service.controller.kafka.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class KeycloakAdminEvent extends KeycloakBaseEvent {
    private String realmId;
    private KeycloakAdminOperation operationType;
    private KeycloakAdminAuthDetails authDetails;
    private String resourceType;
    private String resourcePath;
    private String representation;

    private String error;
    public static KeycloakAdminEvent formKafkaMessage(KeycloakBaseEvent event) {
        final ObjectMapper mapper = new ObjectMapper();
        return new KeycloakAdminEvent()
                .toBuilder()
                .time(event.getTime())
                .realmId(event.getRealmId())
                .unknownFields(Map.of())
                .type(event.getType())
                .operationType(KeycloakAdminOperation.fromString(
                        Optional.ofNullable(
                                (String) event.getUnknownFields().get("operationType")
                        ).orElse("UNKNOWN")
                ))
                .resourceType((String) event.getUnknownFields().get("realmId"))
                .resourcePath((String) event.getUnknownFields().get("resourcePath"))
                .representation((String) event.getUnknownFields().get("representation"))
                .authDetails(
                        mapper.convertValue(event.getUnknownFields().get("authDetails"), KeycloakAdminAuthDetails.class)
                )
                .build();
    }
}