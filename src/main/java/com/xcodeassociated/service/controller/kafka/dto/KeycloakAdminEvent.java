package com.xcodeassociated.service.controller.kafka.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakAdminEvent extends KeycloakBaseEvent {
    private String realmId;
    private String operationType;

    public static KeycloakAdminEvent formKafkaMessage(KeycloakBaseEvent event) {
        return new KeycloakAdminEvent()
                .toBuilder()
                .type(event.getType())
                .realmId((String) event.getUnknownFields().get("realmId"))
                .operationType((String) event.getUnknownFields().get("operationType"))
                .build();
    }
}