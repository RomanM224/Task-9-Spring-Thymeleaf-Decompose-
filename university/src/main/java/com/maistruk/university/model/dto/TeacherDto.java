package com.maistruk.university.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TeacherDto {

    Integer id;
    String firstName;
    String lastName;
}
