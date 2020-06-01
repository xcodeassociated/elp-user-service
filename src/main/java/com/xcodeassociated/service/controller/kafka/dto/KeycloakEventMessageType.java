package com.xcodeassociated.service.controller.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// keycloak docs: https://github.com/keycloak/keycloak/blob/master/server-spi-private/src/main/java/org/keycloak/events/EventType.java

@Getter
public enum KeycloakEventMessageType {
    @JsonProperty("REGISTER")
    REGISTER("REGISTER"),

    @JsonProperty("LOGIN")
    LOGIN("LOGIN"),

    @JsonProperty("LOGOUT")
    LOGOUT("LOGOUT"),

    // unknown type for any other events
    @JsonProperty("UNKNOWN")
    UNKNOWN("UNKNOWN");

    private final String formatted;

    KeycloakEventMessageType(String formatted) {
        this.formatted = formatted;
    }

    private static Map<String, KeycloakEventMessageType> FORMAT_MAP = Stream
            .of(KeycloakEventMessageType.values())
            .collect(Collectors.toMap(s -> s.formatted, Function.identity()));

    @JsonCreator
    public static KeycloakEventMessageType fromString(String string) {
        return Optional
                .ofNullable(FORMAT_MAP.get(string))
                .orElseGet(() -> KeycloakEventMessageType.UNKNOWN);
    }
}
