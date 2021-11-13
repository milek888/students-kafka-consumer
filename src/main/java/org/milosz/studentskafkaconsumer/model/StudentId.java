package org.milosz.studentskafkaconsumer.model;

import java.util.UUID;


import static com.kitopi.api.common.rest.exception.KitopiExceptionUtils.ensureThat;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

public class StudentId extends Id {

    protected StudentId() {
        super();
    }


    StudentId(String id) {
        super(id);
    }

    public static StudentId from(String value) {
        ensureThat(isNotBlank(value), "Value is blank");
        return new StudentId(value);
    }

    public static StudentId unique() {
        return from(UUID.randomUUID().toString());
    }

}
