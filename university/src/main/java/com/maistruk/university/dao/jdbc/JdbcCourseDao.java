package com.maistruk.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.maistruk.university.dao.CourseDao;
import com.maistruk.university.dao.jdbc.rowmapper.CourseRowMapper;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Teacher;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Repository
public class JdbcCourseDao implements CourseDao {

    static final String ADD_COURSE = "INSERT INTO courses (id, name, description) VALUES (DEFAULT, ?, ?)";
    static final String UPDATE_COURSE = "UPDATE courses SET name = ?, description = ? WHERE id = ?";
    static final String DELETE_COURSE = "DELETE FROM courses WHERE id = ?";
    static final String DELETE_TEACHERS_RELATED_TO_COURSE = "DELETE FROM courses_teacher WHERE course_id = ?";
    static final String DELETE_GROUPS_RELATED_TO_COURSE = "DELETE FROM courses_group WHERE course_id = ?";
    static final String DELETE_LESSONS_RELATED_TO_COURSE = "DELETE FROM lessons WHERE course_id = ?";
    static final String GET_ALL_COURSES = "SELECT * FROM courses";
    static final String GET_COURSE_BY_ID = "SELECT * FROM courses WHERE id = ?";
    static final String GET_COURSE_BY_NAME = "SELECT * FROM courses WHERE name = ?";
    static final String GET_COURSES_BY_GROUP_ID = "SELECT courses.id, courses.name, courses.description FROM courses_group "
            + "RIGHT JOIN courses ON courses_group.course_id = courses.id " + "WHERE courses_group.group_id = ?";
    static final String GET_COURSES_BY_TEACHER = "SELECT courses.id, courses.name, courses.description FROM courses_teacher "
            + "LEFT JOIN courses ON courses_teacher.course_id = courses.id WHERE courses_teacher.teacher_id = ?";

    JdbcTemplate jdbcTemplate;
    CourseRowMapper courseRowMapper;

    @Override
    public void create(Course course) {
        log.info(String.format("Create course (%s) [Dao layer]", course.toStringIdName()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(ADD_COURSE, new String[] { "id" });
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            return statement;
        }, keyHolder);
        course.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void update(Course course) {
        log.info(String.format("Update course (%s) [Dao layer]", course.toStringIdName()));
        jdbcTemplate.update(UPDATE_COURSE, course.getName(), course.getDescription(), course.getId());
    }

    @Override
    public void delete(Integer id) {
        log.info(String.format("Delete course by id=%d [Dao layer]", id));
        jdbcTemplate.update(DELETE_TEACHERS_RELATED_TO_COURSE, id);
        jdbcTemplate.update(DELETE_GROUPS_RELATED_TO_COURSE, id);
        jdbcTemplate.update(DELETE_LESSONS_RELATED_TO_COURSE, id);
        jdbcTemplate.update(DELETE_COURSE, id);
    }

    @Override
    public List<Course> getAll() {
        log.info("Get all courses [Dao layer]");
        return jdbcTemplate.query(GET_ALL_COURSES, courseRowMapper);
    }

    @Override
    public Course getById(Integer id) {
        log.info(String.format("Get course by id=%d [Dao layer]", id));
        List<Course> courses = jdbcTemplate.query(GET_COURSE_BY_ID, courseRowMapper, id);
        if (courses.isEmpty()) {
            return null;
        }
        return courses.get(0);
    }

    @Override
    public Course getByName(String name) {
        log.info(String.format("Get course by name=%s [Dao layer]", name));
        List<Course> courses = jdbcTemplate.query(GET_COURSE_BY_NAME, courseRowMapper, name);
        if (courses.isEmpty()) {
            return null;
        }
        return courses.get(0);
    }

    @Override
    public List<Course> getByGroup(Group group) {
        log.info(String.format("Get courses by group (%s) [Dao layer]", group.toString()));
        return jdbcTemplate.query(GET_COURSES_BY_GROUP_ID, courseRowMapper, group.getId());
    }

    @Override
    public List<Course> getCoursesByTeacher(Teacher teacher) {
        log.info(String.format("Get courses by teacher (%s) [Dao layer]", teacher.toString()));
        return jdbcTemplate.query(GET_COURSES_BY_TEACHER, courseRowMapper, teacher.getId());
    }

}
