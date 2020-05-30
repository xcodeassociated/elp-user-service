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
public class KeycloakEvent extends KeycloakBaseEvent {
    private String userId;

    public static KeycloakEvent formKafkaMessage(KeycloakBaseEvent event) {
        return new KeycloakEvent()
                .toBuilder()
                .type(event.getType())
                .userId((String) event.getUnknownFields().get("userId"))
                .build();
    }
}