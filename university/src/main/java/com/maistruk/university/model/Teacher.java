package com.maistruk.university.model;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Teacher {

    Integer id;
    String firstName;
    String lastName;
    List<Course> courses;

    @Override
    public String toString() {
        return String.format("id=%-3d | Name:%-15s | %-15s |", id, firstName, lastName);
    }
    
    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

}
