package org.milosz.studentskafkaconsumer.application;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.util.backoff.FixedBackOff;

import lombok.RequiredArgsConstructor;

@TestConfiguration
@RequiredArgsConstructor
public class KafkaTestConfig {

/*    private final KafkaProperties kafkaProperties;*/

/*    @Bean
    KafkaTemplate<String, String> byteArrayKafkaTemplate() {
        return KafkaTemplate<String, String>.from(kafkaProperties);
    }*/

/*    @Bean
    Consumer kafkaConsumer() {

        def factory = consumerFactory();
        def consumer = factory.createConsumer()
        def topics = kafkaProperties.avro.values()
                .findAll { it -> it.consume }
                .collect { it -> it.name }
        consumer.subscribe(topics)
        return consumer
    }*/

/*    @Bean
    ConsumerFactory consumerFactory() {
        Map<String, Object> consumerProps = kafkaProperties.buildConsumerProperties();
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId4");
        return new DefaultKafkaConsumerFactory<>(consumerProps);
    }


    @Bean(value = "kafkaListenerContainerFactory111111")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(*//*KafkaTemplate<String, String> kafkaTemplate*//*) {
        ConcurrentKafkaListenerContainerFactory<String, String> listenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        listenerContainerFactory.setConsumerFactory(consumerFactory());
*//*        SeekToCurrentErrorHandler errorHandler = new SeekToCurrentErrorHandler(deadLetterPublishingRecoverer(kafkaTemplate), new FixedBackOff(2000L, 1));
        listenerContainerFactory.setErrorHandler(errorHandler);*//*
        return listenerContainerFactory;
    }*/
/*
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(senderProps());
    }

    private Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "alama20kotow");
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //...
        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<String, String>(producerFactory);
    }*/

/*    @Bean
    KafkaTemplate<String, String> kafkaTemplate() {
        Map<String, Object> consumerProps = kafkaProperties.buildConsumerProperties();
        DefaultKafkaProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(consumerProps);
        return new KafkaTemplate<>(producerFactory);
    }*/

}
