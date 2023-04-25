package SwitchAnalyzer.Kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class GenericConsumer {
    private final KafkaConsumer<String, String> consumer;
    private  String topic;

    public GenericConsumer(String IP,String groupId) {
        Properties props = new Properties();
        props.put("bootstrap.servers", IP);
        props.put("group.id", groupId);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //TODO: note this must be removed it is just for testing
       props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        this.consumer = new KafkaConsumer<>(props);

    }
    public void  selectTopic(String topic)
    {
        this.consumer.subscribe(Arrays.asList(topic));

    }
    public ConsumerRecords<String, String> consume(int milliSeconds) {


//        while (true) {
            ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofMillis(milliSeconds));
//            for (ConsumerRecord<String, String> record : records) {
//                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
//            }
//        }
        return records;
    }

    public void close() {
        this.consumer.close();
    }
}



