package com.maistruk.university.mapper;

import org.springframework.stereotype.Component;

import com.maistruk.university.model.Course;
import com.maistruk.university.model.dto.CourseDto;

@Component
public class CourseMapper {

    public Course mapCourseDtoToCourse(CourseDto courseDto) {
        return Course.builder().id(courseDto.getId()).name(courseDto.getName()).description(courseDto.getDescription())
                .build();
    }

}
