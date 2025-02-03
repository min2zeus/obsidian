import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaBookProducer1 {
  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "1-110-TEST-CENT-JYS:9092,148-MFT1-KJW:9092,149-MFT2-KJW:9092");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    Producer<String, String> producer = new KafkaProducer<>(props);
    producer.send(new ProducerRecord<String, String>("rep3-topic", "Apache Kafka is a distributed streaming platform - JAVA TEST MESSAGE"));
    producer.close();
  }
}
