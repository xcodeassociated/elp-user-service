package com.xcodeassociated.service.model.dto.simple;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserSimpleEmailsDto {
    private String firstName;
    private String lastName;
    private Set<String> emails;
}
