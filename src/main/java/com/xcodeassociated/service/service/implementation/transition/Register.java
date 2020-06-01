package com.xcodeassociated.service.service.implementation.transition;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Register extends BaseUserEvent {
    private String email;
    private String username;
}
