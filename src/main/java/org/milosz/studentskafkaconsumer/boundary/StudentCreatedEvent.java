package org.milosz.studentskafkaconsumer.boundary;


import org.milosz.studentskafkaconsumer.model.Student;
import org.milosz.studentskafkaconsumer.model.StudentId;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StudentCreatedEvent {
    StudentId id;

    String name;
    String secondName;
    int age;
    int grade;

    public static StudentCreatedEvent toCommand(Student student) {
        return StudentCreatedEvent.builder()
                .id(student.getId())
                .name(student.getName())
                .secondName(student.getSecondName())
                .grade(student.getGrade())
                .age(student.getAge())
                .build();
    }
}
