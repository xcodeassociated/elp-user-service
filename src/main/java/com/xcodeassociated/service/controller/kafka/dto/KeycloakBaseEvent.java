package com.xcodeassociated.service.controller.kafka.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
public class KeycloakBaseEvent {
    protected KafkaKeycloakMessageType type;

    @JsonIgnore
    protected Map<String, Object> unknownFields = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getUnknownFields() {
        return unknownFields;
    }

    @JsonAnySetter
    public void setUnknownFields(String name, Object value) {
        unknownFields.put(name, value);
    }

}
