package com.xcodeassociated.service.controller.rest.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdateRepresentationDto {
    @JsonProperty("username")
    private String username;

    @JsonProperty("emailVerified")
    private Boolean emailVerified;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("email")
    private String email;
}
