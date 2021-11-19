package org.milosz.studentskafkaconsumer.application

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification

class KafkaTestContainer extends Specification {

    private static final String DOCKER_IMAGE_NAME = "public.ecr.aws/kitopi/cp-kafka:5.4.3"

    private static KafkaContainer kafkaContainer

    static {
        kafkaContainer = new KafkaContainer(DockerImageName.parse(DOCKER_IMAGE_NAME).asCompatibleSubstituteFor("confluentinc/cp-kafka"))
        kafkaContainer.start()
    }

/*    static KafkaContainer getInstance() {
        if (kafkaContainer == null) {
            kafkaContainer = new KafkaContainer(DockerImageName.parse(DOCKER_IMAGE_NAME).asCompatibleSubstituteFor("confluentinc/cp-kafka"))
            kafkaContainer.waitingFor(Wait.forLogMessage(".*successfully elected as the controller.*", 1));
            System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
            System.setProperty("bootstrap.servers", kafkaContainer.getBootstrapServers());
            kafkaContainer.start();
        }
        return kafkaContainer;
    }*/

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.producer.bootstrap-servers", () -> kafkaContainer.getBootstrapServers())
        registry.add("spring.kafka.consumer.bootstrap-servers", () -> kafkaContainer.getBootstrapServers())
    }
}
