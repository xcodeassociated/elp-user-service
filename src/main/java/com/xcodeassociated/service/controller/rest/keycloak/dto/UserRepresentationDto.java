package com.xcodeassociated.service.controller.rest.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @JsonProperty("totp")
    Boolean totp;

    @JsonProperty("notBefore")
    Integer notBefore;

    @JsonProperty("disableableCredentialTypes")
    Set<String> disableableCredentialTypes;

    @JsonProperty("requiredActions")
    List<String> requiredActions;

    @JsonProperty("access")
    Map<String, Boolean> access;

}
