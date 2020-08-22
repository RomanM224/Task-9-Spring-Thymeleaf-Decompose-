package com.maistruk.university.model;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Group {

    Integer id;
    String name;
    List<Student> students;
    List<Course> courses;

    @Override
    public String toString() {
        return String.format("id=%-3d | name:%-7s |", id, name);
    }

}
