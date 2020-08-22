package com.maistruk.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.maistruk.university.dao.TeacherDao;
import com.maistruk.university.dao.jdbc.rowmapper.TeacherRowMapper;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Teacher;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Repository
public class JdbcTeacherDao implements TeacherDao {

    static final String ADD_TEACHER = "INSERT INTO teachers (id, first_name, last_name) VALUES (DEFAULT, ?, ?)";
    static final String UPDATE_TEACHER = "UPDATE teachers SET first_name = ?, last_name = ? WHERE id = ?";
    static final String DELETE_TEACHER = "DELETE FROM teachers WHERE id = ?";
    static final String DELETE_COURSES_RELATED_TO_TEACHER = "DELETE FROM courses_teacher WHERE teacher_id = ?";
    static final String DELETE_LESSONS_RELATED_TO_TEACHER = "DELETE FROM lessons WHERE teacher_id = ?";
    static final String DELETE_COURSE_FROM_TEACHER = "DELETE FROM courses_teacher WHERE course_id = ? AND teacher_id = ?";
    static final String ADD_COURSES_TO_TEACHER = "INSERT INTO courses_teacher (course_id, teacher_id) VALUES (?, ?) ";
    static final String GET_ALL_TEACHERS = "SELECT * FROM teachers";
    static final String GET_TEACHER_BY_ID = "SELECT * FROM teachers WHERE id = ?";
    static final String GET_TEACHER_BY_FULLNAME = "SELECT * FROM teachers WHERE first_name = ? AND last_name = ?";
    static final String GET_TEACHERS_BY_COURSE_ID = "SELECT teachers.id, teachers.first_name, teachers.last_name "
            + "FROM courses_teacher " + "RIGHT JOIN teachers ON courses_teacher.teacher_id = teachers.id "
            + "WHERE courses_teacher.course_id = ?";

    JdbcTemplate jdbcTemplate;
    TeacherRowMapper teacherRowMapper;

    @Override
    public void create(Teacher teacher) {
        log.info(String.format("Create teacher (%s) [Dao layer]", teacher.toString()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(ADD_TEACHER, new String[] { "id" });
            statement.setString(1, teacher.getFirstName());
            statement.setString(2, teacher.getLastName());
            return statement;
        }, keyHolder);
        teacher.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void update(Teacher teacher) {
        log.info(String.format("Update teacher (%s) [Dao layer]", teacher.toString()));
        jdbcTemplate.update(UPDATE_TEACHER, teacher.getFirstName(), teacher.getLastName(), teacher.getId());
    }

    @Override
    public List<Teacher> getAll() {
        log.info("Get all teachers [Dao layer]");
        return jdbcTemplate.query(GET_ALL_TEACHERS, teacherRowMapper);
    }

    @Override
    public Teacher getById(Integer id) {
        log.info(String.format("Get teacher by id=%d [Dao layer]", id));
        List<Teacher> teachers = jdbcTemplate.query(GET_TEACHER_BY_ID, teacherRowMapper, id);
        if (teachers.isEmpty()) {
            return null;
        }
        return teachers.get(0);
    }

    public Teacher getByFullName(String firstName, String lastName) {
        log.info(String.format("Get teacher by full name (%s %s) [Dao layer]", firstName, lastName));
        List<Teacher> teachers = jdbcTemplate.query(GET_TEACHER_BY_FULLNAME, teacherRowMapper, firstName, lastName);
        if (teachers.isEmpty()) {
            return null;
        }
        return teachers.get(0);
    }

    @Override
    public List<Teacher> getByCourse(Course course) {
        log.info(String.format("Get teachers by course (%s) [Dao layer]", course.toStringIdName()));
        return jdbcTemplate.query(GET_TEACHERS_BY_COURSE_ID, teacherRowMapper, course.getId());
    }

    @Override
    public void delete(Integer id) {
        log.info(String.format("Delete teacher by id=%d [Dao layer]", id));
        jdbcTemplate.update(DELETE_COURSES_RELATED_TO_TEACHER, id);
        jdbcTemplate.update(DELETE_LESSONS_RELATED_TO_TEACHER, id);
        jdbcTemplate.update(DELETE_TEACHER, id);
    }

    @Override
    public void deleteCourseFromTeacher(Course course, Teacher teacher) {
        log.info(String.format("Delete course (%s) from teacher (%s) [Dao layer]", course.toStringIdName(),
                teacher.toString()));
        jdbcTemplate.update(DELETE_COURSE_FROM_TEACHER, course.getId(), teacher.getId());
    }

    @Override
    public void addCourseToGroup(Course course, Teacher teacher) {
        log.info(String.format("Add course (%s) to teacher (%s) [Dao layer]", course.toStringIdName(),
                teacher.toString()));
        jdbcTemplate.update(ADD_COURSES_TO_TEACHER, course.getId(), teacher.getId());

    }
}
