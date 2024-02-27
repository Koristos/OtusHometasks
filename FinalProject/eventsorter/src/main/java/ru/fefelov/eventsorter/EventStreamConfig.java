package ru.fefelov.eventsorter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.fefelov.eventsorter.dto.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableKafkaStreams
@Slf4j
public class EventStreamConfig {

    private static final String PREFIX = "B-";
    private static final String NOTIFICATION_TOPIC = "NOTIFICATIONS";
    private static final String ACTIVITY_TOPIC = "ACTIVITY";
    private static final String BONUS_TOPIC = "BONUS";
    private static final String MARKETEVENTS_TOPIC = "MARKETEVENTS";
    @Autowired
    private EventMapper eventMapper;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "eventSorter");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public Serde<Event> eventSerde() {
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(Event.class));
    }

    @Bean
    public Serde<ActivityEvent> activityEventSerde() {
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(ActivityEvent.class));
    }

    @Bean
    public Serde<NotificationEvent> notificationEventSerde() {
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(NotificationEvent.class));
    }

    @Bean
    public Serde<BonusEvent> bonusEventSerde() {
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(BonusEvent.class));
    }

    @Bean
    public KStream<String, Event> kStream(StreamsBuilder kStreamBuilder) {
        KStream<String, String> stream = kStreamBuilder
                .stream(MARKETEVENTS_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));
        stream.foreach((key, value) -> System.out.println(value));
        KStream<String, Event> eventStream = stream
                .mapValues(this::getEventFromString);

        Map<String, KStream<String, Event>> result = eventStream.split(Named.as(PREFIX))
                        .branch((k, v) -> EventType.DEAL.equals(v.getType()), Branched.as(EventType.DEAL.name()))
                        .branch((k, v) -> EventType.CART.equals(v.getType()), Branched.as(EventType.CART.name()))
                        .branch((k, v) -> EventType.LOGIN.equals(v.getType()), Branched.as(EventType.LOGIN.name()))
                        .noDefaultBranch();

        processDeal(result.get(PREFIX+EventType.DEAL.name()));
        processCart(result.get(PREFIX+EventType.CART.name()));
        processLogin(result.get(PREFIX+EventType.LOGIN.name()));
        return eventStream;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    private Event getEventFromString(String eventString) {
        Event event = null;
        try {
            event = objectMapper().readValue(eventString, Event.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return event;
    }

    private void processDeal (KStream<String, Event> stream){
        KStream<String, BonusEvent> os = stream.map((k, v) -> KeyValue.pair(k, eventMapper.toBonusEvent(v)));
        KStream<String, NotificationEvent> nos = stream.map((k, v) -> {
            NotificationEvent ne = eventMapper.toNotificationEvent(v);
            ne.setNotificationType(NotificationType.DEAL);
            return KeyValue.pair(k, ne);
        });
        os.to(BONUS_TOPIC, Produced.with(Serdes.String(), bonusEventSerde()));
        nos.to(NOTIFICATION_TOPIC, Produced.with(Serdes.String(), notificationEventSerde()));
    }

    private void processCart (KStream<String, Event> stream){
        KStream<String, NotificationEvent> os = stream.map((k, v) -> {
            NotificationEvent ne = eventMapper.toNotificationEvent(v);
            ne.setNotificationType(NotificationType.INTEREST);
            return KeyValue.pair(k, ne);
        });
        os.to(NOTIFICATION_TOPIC, Produced.with(Serdes.String(), notificationEventSerde()));
    }

    private void processLogin (KStream<String, Event> stream){
        KStream<String, ActivityEvent> os = stream.map((k, v) -> KeyValue.pair(k, eventMapper.toActivityEvent(v)));
        os.to(ACTIVITY_TOPIC, Produced.with(Serdes.String(), activityEventSerde()));
    }
}
