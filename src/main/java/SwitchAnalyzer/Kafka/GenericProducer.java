package SwitchAnalyzer.Kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class GenericProducer {
    private  KafkaProducer<String, String> producer = null;
    private  KafkaProducer<String, byte[]> byteProducer = null;
    /**
     *
     * @param bootstrapServers should be in this syntax "192.168.1.4:9092"
     */
    public GenericProducer(String bootstrapServers)
    {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
    }

    public GenericProducer(String bootstrapServers, boolean flag)
    {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.44:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        this.byteProducer = new KafkaProducer<>(properties);
    }

    /**
     *
     * @param topic
     * @param value
     */
    public void send( String topic,String value)
    {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
        this.producer.send(record);
    }

    public void send(String topic, byte[] value)
    {
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, value);
        this.byteProducer.send(record);
    }

    public void close() {
        producer.flush();
        this.producer.close();
    }

    public void flush()
    {
        producer.flush();
    }
}
