package com.maistruk.university.model;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Lesson {

    Integer id;
    TimeTable timeTable;
    Course course;
    Group group;
    Teacher teacher;
    Auditory auditory;
    LocalDate date;

    @Override
    public String toString() {
        return "lessonId=" + id + " | " + group + " " + course.toStringIdName() + " " + teacher + " " + auditory + " "
                + timeTable + " " + date.toString();
    }

}
