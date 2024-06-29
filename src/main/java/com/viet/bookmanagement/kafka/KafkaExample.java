package com.viet.bookmanagement.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.Collections;
import java.util.concurrent.Future;

public class KafkaExample {
    private static final String TOPIC = "test-topic";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static void main(String[] args) {
        produceMessage();
        consumeMessage();
    }

    public static void produceMessage() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); //Sets the bootstrap.servers property, which tells the producer the address of the Kafka broker to connect to. In this case, it's localhost:9092.
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); //Sets the key.serializer property to specify the class used to serialize the message keys. Here, it uses the StringSerializer class.
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); //Sets the value.serializer property to specify the class used to serialize the message values. It also uses the StringSerializer class.
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // Set acks to "all" for full acknowledgment

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) { //Creates a KafkaProducer instance with the specified properties, using a try-with-resources statement to ensure that the producer is closed properly after use.
            for (int i = 0; i < 10; i++) {
                String message = "Message " + i;
                ProducerRecord<String, String> record = new ProducerRecord<>("test-topic", Integer.toString(i), message);
//                Future<RecordMetadata> future = producer.send(record); //Sends the message to the Kafka topic named "test-topic", with the key set to the string representation of the loop index i and the value set to the message string.

                Future<RecordMetadata> future = producer.send(record, (metadata, exception) -> {
                    if (exception == null) {
                        System.out.printf("Sent record(key=%s value=%s) " +
                                        "meta(partition=%d, offset=%d)%n",
                                record.key(), record.value(), metadata.partition(), metadata.offset());
                    } else {
                        System.err.printf("Failed to send record(key=%s value=%s) %n",
                                record.key(), record.value());
                        exception.printStackTrace();
                    }
                });

                // Optionally, you can wait for the send to complete synchronously
                future.get(); // This will block until the ack is received

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void consumeMessage() {
        Properties props = new Properties(); //Creates a Properties object to hold the configuration settings for the Kafka consumer.
        props.put("bootstrap.servers", "localhost:9092"); //Sets the bootstrap.servers property to the address of the Kafka broker, localhost:9092.
        props.put("group.id", "test-group"); //Sets the group.id property to "test-group", which identifies the consumer group the consumer belongs to.
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer"); //Sets the key.deserializer property to specify the class used to deserialize the message keys. Here, it uses the StringDeserializer class.
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer"); //Sets the value.deserializer property to specify the class used to deserialize the message values. It also uses the StringDeserializer class.

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) { //Creates a KafkaConsumer instance with the specified properties, using a try-with-resources statement to ensure that the consumer is closed properly after use.
            consumer.subscribe(Collections.singletonList("test-topic")); //Subscribes the consumer to the topic named "test-topic".

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100); //Polls the Kafka broker for new messages, with a timeout of 100 milliseconds. The poll method returns a collection of records.
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Consumed message: offset = %d, key = %s, value = %s%n",
                            record.offset(), record.key(), record.value());
                }
            }
        }
    }

}
