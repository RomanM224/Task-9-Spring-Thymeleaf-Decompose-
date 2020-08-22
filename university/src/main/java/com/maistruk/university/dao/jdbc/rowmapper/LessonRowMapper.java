package com.maistruk.university.dao.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.maistruk.university.model.Auditory;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Lesson;
import com.maistruk.university.model.Teacher;
import com.maistruk.university.model.TimeTable;

@Component
public class LessonRowMapper implements RowMapper<Lesson> {

    @Override
    public Lesson mapRow(ResultSet resultSet, int row) throws SQLException {
        Integer lessonId = resultSet.getInt("id");
        Integer courseId = resultSet.getInt("course_id");
        Course course = Course.builder().id(courseId).build();
        Integer groupId = resultSet.getInt("group_id");
        Group group = Group.builder().id(groupId).build();
        Integer teacherId = resultSet.getInt("teacher_id");
        Teacher teacher = Teacher.builder().id(teacherId).build();
        Integer auditoryId = resultSet.getInt("auditory_id");
        Auditory auditory = Auditory.builder().id(auditoryId).build();
        Integer timeTableId = resultSet.getInt("time_table_id");
        TimeTable timeTable = TimeTable.builder().id(timeTableId).build();
        LocalDate date = LocalDate.parse(resultSet.getString("date"));
        return Lesson.builder().id(lessonId).course(course).group(group).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
    }

}
