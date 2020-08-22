package com.maistruk.university.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student {

    private Integer id;
    private Group group;
    private String firstName;
    private String lastName;

    @Override
    public String toString() {
        return String.format("id=%-3d | Name:%-15s | %-15s |", id, firstName, lastName);
    }

}
