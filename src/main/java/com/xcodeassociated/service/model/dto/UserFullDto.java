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
public class UserFullDto extends BaseEntityDto {
    private Long id;
    private String authId;
    private String username;
    private String firstName;
    private String lastName;
    private Long createdTimestamp;
    private Boolean enabled;
    private Set<ContactDto> contacts;
    private Set<HistoryDto> history;
}
