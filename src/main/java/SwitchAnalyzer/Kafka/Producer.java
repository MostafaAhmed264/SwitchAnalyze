package SwitchAnalyzer.Kafka;

import SwitchAnalyzer.Network.Ports;
import SwitchAnalyzer.miscellaneous.JSONConverter;

public class Producer
{
    GenericProducer producer;
    public Producer(String ip) {  producer = new GenericProducer(ip + ":" + Ports.port1); }
    public void produce(String json, String topic)
    {
        System.out.println(json);
        producer.send(topic,json);
    }
    public void flush() { producer.flush(); }
    public void close() { producer.close(); }
}
