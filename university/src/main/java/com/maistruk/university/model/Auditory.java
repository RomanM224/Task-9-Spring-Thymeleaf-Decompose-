package com.maistruk.university.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Auditory {

    Integer id;
    Integer number;

    @Override
    public String toString() {
        return String.format("id=%-3d | number:%-5s |", id, number);
    }

}
