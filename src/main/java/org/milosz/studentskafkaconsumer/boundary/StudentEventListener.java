package org.milosz.studentskafkaconsumer.boundary;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentEventListener {

    @KafkaListener(topics = "student-topic", groupId = "groupId4")
    public void handleStudentEvent(/*@Payload StudentCreatedEvent studentCreatedEvent*/@Payload String studentCreatedEvent) {
        log.info(studentCreatedEvent);
        throw new RuntimeException("exception 123456");
    }

}
