package org.milosz.studentskafkaconsumer.application;


import org.milosz.studentskafkaconsumer.model.StudentId;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateStudentCommand {
    StudentId id;

    String name;
    String secondName;
    int age;
    int grade;
}
