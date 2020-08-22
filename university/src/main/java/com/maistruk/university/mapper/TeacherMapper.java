package com.maistruk.university.mapper;

import org.springframework.stereotype.Component;

import com.maistruk.university.model.Teacher;
import com.maistruk.university.model.dto.TeacherDto;

@Component
public class TeacherMapper {

    public Teacher mapTeacherDtoToTeacher(TeacherDto teacherDto) {
        return Teacher.builder().id(teacherDto.getId()).firstName(teacherDto.getFirstName())
                .lastName(teacherDto.getLastName()).build();
    }

}
