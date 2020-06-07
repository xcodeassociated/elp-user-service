package com.xcodeassociated.service.controller.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum KeycloakAdminOperation {
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    ACTION("ACTION"),
    UNKNOWN("UNKNOWN");

    private final String formatted;

    KeycloakAdminOperation(String formatted) {
        this.formatted = formatted;
    }

    private static Map<String, KeycloakAdminOperation> FORMAT_MAP = Stream
            .of(KeycloakAdminOperation.values())
            .collect(Collectors.toMap(s -> s.formatted, Function.identity()));

    public String getFormatted() {
        return this.formatted;
    }

    @JsonCreator
    public static KeycloakAdminOperation fromString(String string) {
        return Optional
                .ofNullable(FORMAT_MAP.get(string))
                .orElseGet(() -> KeycloakAdminOperation.UNKNOWN);
    }
}
