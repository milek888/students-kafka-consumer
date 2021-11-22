package org.milosz.studentskafkaconsumer.boundary;

import org.milosz.studentskafkaconsumer.application.Students;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/*@Component*/
@RequiredArgsConstructor
public class StudentEventListener {

    private final Students students;

    @KafkaListener(id = "milek11",autoStartup = "true",topics = "student-topic", groupId = "groupId4"/*, containerFactory = "greetingKafkaListenerContainerFactory"*/)
    public void handleStudentEvent(/*@Payload StudentCreatedEvent studentCreatedEvent*/@Payload String studentCreatedEvent) {
        log.info("Ala ma kota i szedl grzes przez wies");
        students.saveStudent(studentCreatedEvent);
        throw new RuntimeException("exception 123456");
    }

/*
    @KafkaListener(id = "kasia22",autoStartup = "true",topics = "student-topic", groupId = "groupId3"*/
/*, containerFactory = "greetingKafkaListenerContainerFactory"*//*
)
    public void handleStudentEvent2(*/
/*@Payload StudentCreatedEvent studentCreatedEvent*//*
@Payload String studentCreatedEvent) {
        log.info("Ala ma kota i szedl grzes przez wies");
        students.saveStudent(studentCreatedEvent);
        */
/*        throw new RuntimeException("exception 123456");*//*

    }
*/

}
