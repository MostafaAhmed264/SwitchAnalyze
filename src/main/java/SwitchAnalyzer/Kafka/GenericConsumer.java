package SwitchAnalyzer.Kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class GenericConsumer {
    private  KafkaConsumer<String, String> consumer;
    private KafkaConsumer<String, byte[]> byteArrayConsumer;
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
    public GenericConsumer(String IP,String groupId,boolean flag) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,IP);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        this.byteArrayConsumer = new KafkaConsumer<String,byte[]>(properties);
    }
    public void  selectTopicByteArray(String topic)
    {
        this.byteArrayConsumer.subscribe(Arrays.asList(topic));

    }
    public void  selectTopic(String topic)
    {
        this.consumer.subscribe(Arrays.asList(topic));

    }
    public ConsumerRecords<String, String> consume(int milliSeconds) {
            ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofMillis(milliSeconds));

        return records;
    }
    public ConsumerRecords<String, byte[]> consumeByteArray(int milliSeconds) {
        ConsumerRecords<String, byte[]> records = this.byteArrayConsumer.poll(Duration.ofMillis(milliSeconds));
        return records;
    }


    public void close() {
        this.consumer.close();
    }
}



