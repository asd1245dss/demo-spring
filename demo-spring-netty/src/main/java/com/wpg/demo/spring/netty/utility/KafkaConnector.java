package com.wpg.demo.spring.netty.utility;

import lombok.Data;
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
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.jms.*;
import java.util.*;

import static com.alibaba.fastjson.JSON.parseObject;

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
                }
            });
        });

        new Thread(KafkaConnector::stream).start();
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
            }
        }
    }

    private static void stream() {
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "onlineData");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Double().getClass());
//        config.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, EventTimeExtractor.class);

        KStreamBuilder streamBuilder = new KStreamBuilder();
        KStream<String, String> onlineData = streamBuilder.stream(Serdes.String(), Serdes.String(),"my-topic");
        KStream<String, Double> serviceData = onlineData
                .filter((k, v) -> v.contains("2017052701"))
                .flatMap((k, v) -> {
                    RealtimeData realtimeData = parseObject(v, RealtimeData.class);
                    String deviceId = realtimeData.getDeviceId();
                    String dateTime = DateTime.parse(realtimeData.getTimestamp(), dateTimeFormat).toString("yyyyMMddHHmm");

                    Map<String, String> valuesMap = realtimeData.getValues();
                    List<KeyValue<String, Double>> resut = new ArrayList<>(valuesMap.size());
                    valuesMap.forEach((key, values) -> resut.add(new KeyValue<>(String.format("%s|%s|%s", dateTime, deviceId, key), Double.parseDouble(values))));

                    return resut;
                });
//        KTable<String, Double> wordAvg = serviceData
//                .groupByKey()
//                .reduce(((value1, value2) -> value1 + value2));

        KTable<String, Long> wordCount = serviceData
                .groupByKey()
                .count("CountsA");

//        wordAvg.to(Serdes.String(), Serdes.Double(), "avg-onlineData-topic");
        wordCount.to(Serdes.String(), Serdes.Long(), "count-onlineData-topic");
        KafkaStreams streams = new KafkaStreams(streamBuilder, config);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

}

@Data
class RealtimeData {
    private String deviceId;
    private String timestamp;
    private Map<String, String> values;
}

class EventTimeExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long previousTimestamp) {
        RealtimeData realtimeData = (RealtimeData) record.value();
        return DateTime.parse(realtimeData.getTimestamp()).getMillis();
    }
}
