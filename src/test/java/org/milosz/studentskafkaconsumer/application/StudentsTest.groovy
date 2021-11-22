package org.milosz.studentskafkaconsumer.application

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.milosz.studentskafkaconsumer.boundary.StudentEventListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.MessageListenerContainer
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.milosz.studentskafkaconsumer.infrastructure.KafkaConfig

import java.time.Duration;
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.timeout
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = [KafkaConfig.class,/*KafkaProperties.class,*/ KafkaAutoConfiguration.class, StudentEventListener.class, Students.class/*, KafkaTestConfig.class TODO moze bedziemy tego potrzebowac*/])
/*@TestPropertySource(properties = {"spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
        properties = {
            "spring.kafka.consumer.concurrency=1",
            "spring.main.allow-bean-definition-overriding=true",
            "spring.kafka.consumer.auto-offset-reset=earliest",
            "kitopi.checker-api.messaging.kafka.consumer.default-concurrency=2"
        }*/
class StudentsTest extends KafkaTestContainer {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ConsumerFactory<String, String> consumerFactory;

    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

/*    @SpyBean*/
    @Autowired
    StudentEventListener eventListener;

    @SpyBean
    Students students;

    def 'should get looker cookbook'() {
        given:

            for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry.allListenerContainers) {
                ContainerTestUtils.waitForAssignment(messageListenerContainer, 1);
            } // <------ to jest potrzebne zeby test zadzialal
            //Map<String, Object> consumerProps = new HashMap<>(KafkaTestUtils.consumerProps(System.getenv("spring.kafka.consumer.bootstrap-servers"), "2", "false"));
            Properties properties = new Properties();
            properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
            Consumer testConsumer = consumerFactory.createConsumer("dlt-group", "suffix11", "prefix11", properties);
            testConsumer.subscribe(List.of("student-topic.dlt"))
        when:
            kafkaTemplate.send("student-topic", "test message 111");
            CountDownLatch countDownLatch = new CountDownLatch(1)
            countDownLatch.await(3, TimeUnit.SECONDS)
        then:

            verify(students, times(2)).saveStudent(isA(String.class))

            Thread.sleep(2000l)

            ConsumerRecords<String, String> consumerRecords = testConsumer.poll(Duration.ofMillis(2000l));
            var consumedRecord = consumerRecords.iterator().next();
            assertThat(consumedRecord.value()).as("message").isEqualTo("test message 111");
    }

}

/*
  1. Wysyla poprawna wiadomosc na topic - sprawdzamy wywolanie komendy na fasadzie
     i powinien spowodowac wywolanie fasady times(1), z komenda odpowiadajaca eventowi
  2. Wysyla bledna wiadomosc na topic - sprawdzamy retry
     Jesli retry jest n razy to fasada powinna wywolac sie n razy
     Czy sprawdzamy ze wiadomosc pojawila sie na DLT ???
* */
