package com.xcodeassociated.service.controller.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum KafkaKeycloakMessageType {
    // keycloak events
    @JsonProperty("REGISTER")
    REGISTER("REGISTER"),
    @JsonProperty("LOGIN")
    LOGIN("LOGIN"),
    @JsonProperty("LOGOUT")
    LOGOUT("LOGOUT"),
    @JsonProperty("CODE_TO_TOKEN")
    CODE_TO_TOKEN("CODE_TO_TOKEN"),
    // unknown
    @JsonProperty("UNKNOWN")
    UNKNOWN("UNKNOWN");

    private static Map<String, KafkaKeycloakMessageType> FORMAT_MAP = Stream
            .of(KafkaKeycloakMessageType.values())
            .collect(Collectors.toMap(s -> s.formatted, Function.identity()));

    private final String formatted;

    KafkaKeycloakMessageType(String formatted) {
        this.formatted = formatted;
    }

    @JsonCreator
    public static KafkaKeycloakMessageType fromString(String string) {
        return Optional
                .ofNullable(FORMAT_MAP.get(string))
                .orElseGet(() -> KafkaKeycloakMessageType.UNKNOWN);
    }
}
