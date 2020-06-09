package com.xcodeassociated.service.exception.codes;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // generic:
    E000("Unknown error"),
    E001("User not found by id: %d"),
    E002("Error: %s"),
    E003("User not found by email: %s"),
    E004("User not found by auth id: %s"),
    // service:
    S000("Service exception: %s"),
    // kafka:
    K000("Kafka exception: %s"),
    // validation:
    V000("Validation exception: %s"),
    // keycloak api:
    A000("Could not fetch data from keycloak api: %s");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
