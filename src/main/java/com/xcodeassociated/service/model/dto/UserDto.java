package com.xcodeassociated.service.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserDto extends BaseEntityDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Set<ContactDto> contacts;
}
