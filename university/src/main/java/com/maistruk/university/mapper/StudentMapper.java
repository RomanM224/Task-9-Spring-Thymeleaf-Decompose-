package com.maistruk.university.mapper;

import org.springframework.stereotype.Component;

import com.maistruk.university.model.Student;
import com.maistruk.university.model.dto.StudentDto;

@Component
public class StudentMapper {

    public Student mapStudentDtoToStudent(StudentDto studentDto) {
        return Student.builder().id(studentDto.getId()).firstName(studentDto.getFirstName())
                .lastName(studentDto.getLastName()).build();
    }
}
