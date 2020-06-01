package com.xcodeassociated.service.controller.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakAdminAuthDetails {
    @JsonProperty("realmId")
    private String realmId;

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("ipAddress")
    private String ipAddress;
}
