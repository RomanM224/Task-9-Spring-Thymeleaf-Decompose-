package com.maistruk.university.mapper;

import org.springframework.stereotype.Component;

import com.maistruk.university.model.Auditory;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Lesson;
import com.maistruk.university.model.Teacher;
import com.maistruk.university.model.TimeTable;
import com.maistruk.university.model.dto.LessonDto;

@Component
public class LessonMapper {

    public Lesson mapLessonDtoToLesson(LessonDto lessonDto) {
        Group group = Group.builder().id(lessonDto.getGroupId()).build();
        Course course = Course.builder().id(lessonDto.getCourseId()).build();
        Teacher teacher = Teacher.builder().id(lessonDto.getTeacherId()).build();
        Auditory auditory = Auditory.builder().id(lessonDto.getAuditoryId()).build();
        TimeTable timeTable = TimeTable.builder().id(lessonDto.getTimeTableId()).build();
        return Lesson.builder().id(lessonDto.getLessonId()).group(group).course(course).teacher(teacher)
                .auditory(auditory).timeTable(timeTable).date(lessonDto.getDate()).build();
    }

}
