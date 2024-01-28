package ru.fefelov.kafka;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.state.Stores;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class StreamConsumer {

    private static final String HOST = "localhost:9091";
    private static final String APP_ID = "gen-id";
    private static final String TOPIC = "events";
    public static final String STORAGE = "count-record-storage";
    private static final String LOG_TOPIC = APP_ID + "-" + STORAGE + "-changelog";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Map<String, Object> streamsConfig = Map.of(
                StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, HOST,
                StreamsConfig.APPLICATION_ID_CONFIG, APP_ID);

        initTopics();
        Serde<String> stringSerde = Serdes.String();
        StreamsBuilder builder = new StreamsBuilder();
        builder.addStateStore(Stores.keyValueStoreBuilder(Stores.inMemoryKeyValueStore(STORAGE),
                stringSerde, Serdes.Integer()));
        builder.stream(TOPIC, Consumed.with(stringSerde, stringSerde))
                .processValues(CountRecordsProcessor::new, STORAGE)
                .foreach((k,v) -> System.out.println(String.format("In 5 minutes %d messages  with key %s found", v, k)));
        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), new StreamsConfig(streamsConfig));
        kafkaStreams.cleanUp();
        kafkaStreams.start();
        Thread.sleep(300 * 1000);
        kafkaStreams.close();
        System.out.println("App closed");
    }

    private static void initTopics() throws InterruptedException, ExecutionException {
        Map<String, Object> adminConfig = Map.of(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
        Admin admin = Admin.create(adminConfig);
        List<String> topicStrings = admin.listTopics()
                .listings()
                .get()
                .stream()
                .map(TopicListing::name)
                .toList();
        if (topicStrings.contains(LOG_TOPIC)) {
            admin.deleteTopics(List.of(LOG_TOPIC));
            Thread.sleep(2000);
        }
        if (!topicStrings.contains(TOPIC)) {
            admin.createTopics(List.of(new NewTopic(TOPIC, 1, (short) 1)));
            Thread.sleep(2000);
        }

    }
}