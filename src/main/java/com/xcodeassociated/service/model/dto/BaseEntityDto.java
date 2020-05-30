package com.xcodeassociated.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public class BaseEntityDto {
    protected Long id;
    protected long createdDate;
    protected long modifiedDate;
    protected String createdBy;
    protected String modifiedBy;
}
