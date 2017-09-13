package com.wpg.demo.spring.netty.utility;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.jms.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * @author ChangWei Li
 * @version 2017-09-13 09:53
 */
@Slf4j
public class KafkaConnector {

    public static void main(String[] args) throws JMSException {

        String mqHost = System.getenv("MQ_HOST");

        if (mqHost == null) {
            log.error("It seems that you don't set MQ_HOST environment parameters !");
            return;
        }

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(mqHost);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Destination topic = session.createTopic("*.SDSC.PUT");
        MessageConsumer messageConsumer = session.createConsumer(topic);

        Producer<String, String> producer = produce();

        messageConsumer.setMessageListener(message -> {
            ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) message;

            String recieveMsg = new String(bytesMessage.getContent().data);

            producer.send(new ProducerRecord<>("my-topic", recieveMsg), (metadata, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                    log.error("send message failed");
                } else {
                    log.info("my-topic => {}", recieveMsg);
                }
            });
        });
    }

    private static Producer<String, String> produce() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return new KafkaProducer<>(props);
    }

    private static void consume() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("group.id", "test");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "100000");
        properties.put("auto.offset.reset", "earliest");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        Consumer<String, String> consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Collections.singletonList("my-topic"));

        boolean exit = false;

        while (!exit) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                if ("exit".equals(record.value())) {
                    exit = true;
                    break;
                }

                log.info("offset = {}, key = {}, value = {}{}", record.offset(), record.key(), record.value());
            }
        }
    }

}
