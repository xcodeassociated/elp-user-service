package com.xcodeassociated.service.model.dto;

import com.xcodeassociated.service.model.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class HistoryDto extends BaseEntityDto {
    private EventType event;
    private Long timestamp;
}
