package com.xcodeassociated.service.config.kafka.interceptor;

import com.xcodeassociated.commons.correlationid.base.ThreadCorrelationId;
import com.xcodeassociated.commons.correlationid.kafka.KafkaRecordInterceptor;
import com.xcodeassociated.service.controller.kafka.dto.KeycloakBaseEvent;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageWrapperInterceptor extends KafkaRecordInterceptor<String, KeycloakBaseEvent> {

    public KafkaMessageWrapperInterceptor(ThreadCorrelationId threadCorrelationId) {
        super(threadCorrelationId);
    }

}
