package ru.fefelov.otus.transactions;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Consumer {

    public static void main(String[] args) {

        Map<String, Object> consumerConfig = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, "consumer -" + UUID.randomUUID(),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");


        KafkaConsumer<String, String> enternalConsumer = new KafkaConsumer<>(consumerConfig);
        try (enternalConsumer) {
            enternalConsumer.subscribe(List.of("topic1", "topic2"));
            while (true) {
                ConsumerRecords<String, String> batch = enternalConsumer.poll(Duration.ofSeconds(1));
                batch.forEach(record -> System.out.println(record.topic() + " - " + record.value()));
            }
        }
    }
}
