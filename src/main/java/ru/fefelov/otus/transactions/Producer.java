package ru.fefelov.otus.transactions;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;

public class Producer {
    public static void main(String[] args) {

        Map<String, Object> producerConfig = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                ProducerConfig.ACKS_CONFIG, "all",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.TRANSACTIONAL_ID_CONFIG, "sample");

        KafkaProducer<String, String> producer = new KafkaProducer<>(producerConfig);
        producer.initTransactions();
        producer.beginTransaction();
        for (int i = 0; i < 5; i++) {
            producer.send(new ProducerRecord<>("topic1", "Approved Transaction Message - " + (i+1)));
            producer.send(new ProducerRecord<>("topic2", "Approved Transaction Message - " + (i+1)));
        }
        producer.commitTransaction();

        producer.beginTransaction();
        for (int i = 0; i < 2; i++) {
            producer.send(new ProducerRecord<>("topic1", "Cancelled Transaction Message - " + (i+1)));
            producer.send(new ProducerRecord<>("topic2", "Cancelled Transaction Message - " + (i+1)));
        }
        producer.abortTransaction();
        producer.close();
    }
}
