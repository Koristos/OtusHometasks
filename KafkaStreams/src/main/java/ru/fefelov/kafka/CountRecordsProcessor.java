package ru.fefelov.kafka;

import org.apache.kafka.streams.processor.api.FixedKeyProcessor;
import org.apache.kafka.streams.processor.api.FixedKeyProcessorContext;
import org.apache.kafka.streams.processor.api.FixedKeyRecord;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Date;

public class CountRecordsProcessor  implements FixedKeyProcessor<String, String, Integer> {

    private FixedKeyProcessorContext<String, Integer> context;
    private KeyValueStore<String, Integer> store;
    private Date startDate;

    @Override
    public void init(FixedKeyProcessorContext<String, Integer> context) {
        this.context = context;
        this.store = context.getStateStore(StreamConsumer.STORAGE);
        this.startDate = new Date();
        System.out.println("App started");
    }

    @Override
    public void process(FixedKeyRecord<String, String> fixedKeyRecord) {
        Date timeStamp = new Date(fixedKeyRecord.timestamp());
        if (timeStamp.after(startDate)) {
            String key = fixedKeyRecord.key() == null ? "null" : fixedKeyRecord.key();
            Integer accumulatedBefore = store.get(key);
            if (accumulatedBefore != null) {
                store.put(key, accumulatedBefore + 1);
            } else {
                store.put(key, 1);
            }
            context.forward(fixedKeyRecord.withValue(store.get(key)));
        }
    }
}
