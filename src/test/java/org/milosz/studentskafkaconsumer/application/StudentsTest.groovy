package org.milosz.studentskafkaconsumer.application

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.milosz.studentskafkaconsumer.boundary.StudentEventListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.context.ApplicationContext
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.MessageListenerContainer
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.context.TestPropertySource

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.timeout
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

// tutaj nie potrafi pobrac do KafkaProperties customer.bootstrap i producer
//@SpringBootTest(classes = [KafkaProperties.class, /*KafkaAutoConfiguration.class,*/ StudentEventListener.class, Students.class, KafkaTestConfig.class])

@SpringBootTest(classes = [/*KafkaProperties.class,*/ KafkaAutoConfiguration.class, StudentEventListener.class, Students.class/*, KafkaTestConfig.class*/])
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
    ApplicationContext applicationContext;

    @Autowired
    ConsumerFactory consumerFactory;

    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    Consumer<String, String> kafkaConsumer;

/*    @SpyBean*/@Autowired
    StudentEventListener eventListener;

    @SpyBean
    Students students;

    def 'should get looker cookbook'() {
        given:

            for(MessageListenerContainer messageListenerContainer :kafkaListenerEndpointRegistry.allListenerContainers) {
                ContainerTestUtils.waitForAssignment(messageListenerContainer, 1);
            }
            String test = 'sssss'
            Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(System.out::println)
        when:
/*            Map<String, Object> consumerProps = new HashMap<>(KafkaTestUtils.);
            kafkaConsumer = new DefaultKafkaConsumerFactory(consumerProps);*/
         /*   Consumer kafkaConsumer = consumerFactory.createConsumer();*/

            kafkaTemplate.send("student-topic", "test message 111");
            CountDownLatch countDownLatch = new CountDownLatch(1)
            countDownLatch.await(3, TimeUnit.SECONDS)
        then:
            consumerFactory.getListeners().forEach(listener -> {System.out.println(listener.toString())})

            verify(students, times(1)).saveStudent(isA(String.class));
    }

}

/*
  1. Wysyla poprawna wiadomosc na topic - sprawdzamy wywolanie komendy na fasadzie
     i powinien spowodowac wywolanie fasady times(1), z komenda odpowiadajaca eventowi
  2. Wysyla bledna wiadomosc na topic - sprawdzamy retry
     Jesli retry jest n razy to fasada powinna wywolac sie n razy
     Czy sprawdzamy ze wiadomosc pojawila sie na DLT ???
* */
