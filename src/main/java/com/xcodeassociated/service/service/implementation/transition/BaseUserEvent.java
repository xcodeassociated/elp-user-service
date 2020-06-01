package com.xcodeassociated.service.service.implementation.transition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class BaseUserEvent {
    private Long time;
    private String userId;
    private String realmId;
    private String error;
}
