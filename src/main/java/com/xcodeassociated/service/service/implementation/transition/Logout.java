package com.xcodeassociated.service.service.implementation.transition;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Logout extends BaseUserEvent {
    private String sessionId;
}
