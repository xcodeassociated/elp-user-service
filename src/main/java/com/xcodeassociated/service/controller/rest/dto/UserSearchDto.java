package com.xcodeassociated.service.controller.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserSearchDto {
    @JsonProperty(value = "first_name", required = true)
    private String firstName;

    @JsonProperty(value = "last_name", required = true)
    private String lastName;

    @JsonProperty(value = "email")
    private String email;
}
