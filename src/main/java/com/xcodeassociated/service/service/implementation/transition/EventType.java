package com.xcodeassociated.service.service.implementation.transition;

import java.util.Arrays;
import java.util.Optional;

public enum EventType {
    REGISTER("REGISTER"),
    LOGIN("LOGIN"),
    LOGOUT("LOGOUT"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static Optional<EventType> fromString(String value) {
        return Arrays.stream(EventType.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findAny();
    }

}
