package org.milosz.studentskafkaconsumer.model;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.ToString;


import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Embeddable
@ToString
@EqualsAndHashCode
@MappedSuperclass
public class Id implements Serializable {

    private final String id;

    /**
     * Hibernate stuff
     */
    protected Id() {
        this.id = null;
    }

    protected Id(String id) {
        this.id = id;
    }

    public static Id from(String value) {
        checkArgument(isNotBlank(value), "Value is blank");
        return new Id(value);
    }

    public static Id unique() {
        return from(UUID.randomUUID().toString());
    }
}
