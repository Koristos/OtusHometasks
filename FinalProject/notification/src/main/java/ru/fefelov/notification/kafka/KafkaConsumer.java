package ru.fefelov.notification.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.fefelov.notification.dto.NotificationEvent;
import ru.fefelov.notification.NotificationProcessor;

@Component
@Slf4j
public class KafkaConsumer {

    private static final String TOPIC = "NOTIFICATIONS";
    @Autowired
    private NotificationProcessor notificationProcessor;

    @KafkaListener(topics = TOPIC, groupId = "group_notif")
    public void consume(String message) {
        notificationProcessor.process(getEventFromString(message));
    }

    private NotificationEvent getEventFromString(String eventString) {
        NotificationEvent event = null;
        try {
            event = KafkaConfig.objectMapper().readValue(eventString, NotificationEvent.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return event;
    }
}
