package ru.fefelov.summer.market.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.fefelov.summer.market.events.Event;

@Service
public class EventKafkaProducer {

    @Autowired
    private KafkaTemplate<String, Event> kafkaTemplate;

    @Value("${app.kafka.producer.topic}")
    private String topic;

    public void send(Event message) {
        kafkaTemplate.send(topic, message);
    }
}
