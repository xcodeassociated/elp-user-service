package com.xcodeassociated.service.service.implementation.transition;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Update extends BaseUserEvent {

}
