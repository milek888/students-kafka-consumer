package org.milosz.studentskafkaconsumer.infrastructure;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.xml.sax.ErrorHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import static java.lang.String.format;

/*@EnableKafka*/
@Configuration
@Slf4j
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
/*        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");  <------ TO POTRZEBUJEMY KONIECZNIE zeby mogl odczytac z propertiesow w yaml to musimy uzyc KafkaProperties kafkaProperties
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id4"); <------ tego nie potrzebujemy koniecznie
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);  <------ to sobie zazwyczaj w osobnym @Bean ustawiamy
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); <------ to sobie zazwyczaj w osobnym @Bean ustawiamy
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");*/
        DefaultKafkaConsumerFactory<String, String> defaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties());
        return defaultKafkaConsumerFactory;
    }

    // @Bean(name = "greetingKafkaListenerContainerFactory")
    @Bean(name = "kafkaListenerContainerFactory")   // <------ dzieki tej nazwie wezmie to factory a nie factory z Autoconfigu bo w KafkaAutoConfiguration -> @Import -> KafkaAnnotationDrivenConfiguration mamy
    //@ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
    //	ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
    //w przeciwnym razie wezmie wlasnie z deafolta factory
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(KafkaTemplate<String, String> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, String> listenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        listenerContainerFactory.setConsumerFactory(consumerFactory());
        SeekToCurrentErrorHandler errorHandler = new SeekToCurrentErrorHandler(deadLetterPublishingRecoverer(kafkaTemplate), new FixedBackOff(2000L, 1));
        listenerContainerFactory.setErrorHandler(errorHandler);
        listenerContainerFactory.setConcurrency(6);
        return listenerContainerFactory;
    }

/*    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        DefaultKafkaProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
        return new KafkaTemplate<>(producerFactory);
    }*/

    /*    @Autowired
        private KafkaTemplate<String, String> kafkaTemplate;*/

    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaTemplate<?, ?> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(kafkaTemplate, (cr, e) -> {
            var topicPartition = new TopicPartition(format("%s%s", cr.topic(), ".dlt"),
                    cr.partition());
            log.error(String.format("Failed to process event (key: '%s'). Publishing to DLT: '%s'", cr.key(), topicPartition.topic()), e);
            return topicPartition;
        });
    }

}