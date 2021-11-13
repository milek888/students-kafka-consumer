package org.milosz.studentskafkaconsumer.model;

import org.milosz.studentskafkaconsumer.application.CreateStudentCommand;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Student {

    @EmbeddedId
    StudentId id;

    String name;
    String secondName;
    int age;
    int grade;

    public static Student of(CreateStudentCommand createStudentCommand) {
        return new Student(createStudentCommand.getId(), createStudentCommand.getName(), createStudentCommand.getSecondName(),
                createStudentCommand.getGrade(), createStudentCommand.getAge());
    }
}
