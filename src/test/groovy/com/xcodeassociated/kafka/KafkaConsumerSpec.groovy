package com.xcodeassociated.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.xcodeassociated.service.controller.kafka.consumer.KafkaConsumer
import com.xcodeassociated.service.controller.kafka.dto.KeycloakBaseEvent
import com.xcodeassociated.service.controller.kafka.dto.KeycloakEvent
import com.xcodeassociated.service.service.implementation.UserEventService
import groovy.util.logging.Slf4j
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@Slf4j
@ActiveProfiles("test")
class KafkaConsumerSpec extends Specification {

    def userEventService = Mock(UserEventService)
    def kafkaConsumer = new KafkaConsumer(userEventService)

    def "should receive client order event via kafka consumer"() {
        given:
            ObjectMapper mapper = new ObjectMapper()
            KeycloakBaseEvent message = mapper
                    .readValue(new File('src/test/resources/dto/KafkaMessageWrapper.json'), KeycloakBaseEvent.class)
        when:
            kafkaConsumer.onKeycloakEvent(message, 0, 0)
        then:
            1 * userEventService.handleKeycloakEvent(KeycloakEvent.formKafkaMessage(message))
    }
}
