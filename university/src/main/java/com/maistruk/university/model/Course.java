package com.maistruk.university.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {

    Integer id;
    String name;
    String description;

    @Override
    public String toString() {
        return String.format("id=%-3d | name:%-20s | description:%-210s |", id, name, description);
    }

    public String toStringIdName() {
        return String.format("id=%-3d | name:%-18s |", id, name);
    }

}
