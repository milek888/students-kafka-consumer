package org.milosz.studentskafkaconsumer.application;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Students {

    public void saveStudent(String studentCreatedEvent) {
        log.info(studentCreatedEvent);
    }

}
