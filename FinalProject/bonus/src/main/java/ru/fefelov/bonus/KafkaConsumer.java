package ru.fefelov.bonus;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

    private static final String TOPIC = "BONUS";
    @Autowired
    private BonusEventProcessor bonusEventProcessor;

    @KafkaListener(topics = TOPIC, groupId = "group_bonus")
    public void consume(String message) {
        bonusEventProcessor.process(getEventFromString(message));
    }

    private BonusEvent getEventFromString(String eventString) {
        BonusEvent event = null;
        try {
            event = KafkaConfig.objectMapper().readValue(eventString, BonusEvent.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return event;
    }
}
