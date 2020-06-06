package com.xcodeassociated.service.controller.rest.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserRepresentationDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("createdTimestamp")
    private Long createdTimestamp;

    @JsonProperty("username")
    private String username;

    @JsonProperty("enabled")
    private Boolean enabled;

    @JsonProperty("emailVerified")
    private Boolean emailVerified;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("email")
    private String email;
}
