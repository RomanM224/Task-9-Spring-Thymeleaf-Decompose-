package com.maistruk.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.maistruk.university.dao.GroupDao;
import com.maistruk.university.dao.jdbc.rowmapper.GroupRowMapper;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Student;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Repository
public class JdbcGroupDao implements GroupDao {

    static final String ADD_GROUP = "INSERT INTO groups (id, name) VALUES (DEFAULT, ?) ";
    static final String UPDATE_GROUP = "UPDATE groups SET name = ? WHERE id = ?";
    static final String DELETE_GROUP = "DELETE FROM groups WHERE id = ?";
    static final String DELETE_STUDENTS_RELATED_TO_GROUP = "DELETE FROM students_group WHERE group_id = ?";
    static final String DELETE_COURSES_RELATED_TO_GROUP = "DELETE FROM courses_group WHERE group_id = ?";
    static final String DELETE_LESSONS_RELATED_TO_GROUP = "DELETE FROM lessons WHERE group_id = ?";
    static final String DELETE_STUDENT_FROM_GROUP = "DELETE FROM students_group WHERE student_id = ?";
    static final String ADD_STUDENTS_TO_GROUP = "INSERT INTO students_group (student_id, group_id) VALUES (?, ?) ";
    static final String DELETE_COURSE_FROM_GROUP = "DELETE FROM courses_group WHERE course_id = ? AND group_id = ?";
    static final String GET_COURSES_BY_GROUP_ID = "SELECT courses.id, courses.name, courses.description FROM courses_group "
            + "LEFT JOIN courses ON courses_group.course_id = courses.id WHERE courses_group.group_id = ?";
    static final String ADD_COURSES_TO_GROUP = "INSERT INTO courses_group (course_id, group_id) VALUES (?, ?) ";
    static final String GET_ALL_GROUPS = "SELECT * FROM groups";
    static final String GET_GROUP_BY_ID = "SELECT * FROM groups WHERE id = ? ";
    static final String GET_GROUP_BY_NAME = "SELECT * FROM groups WHERE name = ? ";

    JdbcTemplate jdbcTemplate;
    GroupRowMapper groupRowMapper;

    @Override
    public void create(Group group) {
        log.info(String.format("Create group (%s) [Dao layer]", group.toString()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(ADD_GROUP, new String[] { "id" });
            statement.setString(1, group.getName());
            return statement;
        }, keyHolder);
        group.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void update(Group group) {
        log.info(String.format("Update group (%s) [Dao layer]", group.toString()));
        jdbcTemplate.update(UPDATE_GROUP, group.getName(), group.getId());
    }

    @Override
    public void delete(Integer id) {
        log.info(String.format("Delete group by id=%d [Dao layer]", id));
        jdbcTemplate.update(DELETE_STUDENTS_RELATED_TO_GROUP, id);
        jdbcTemplate.update(DELETE_COURSES_RELATED_TO_GROUP, id);
        jdbcTemplate.update(DELETE_LESSONS_RELATED_TO_GROUP, id);
        jdbcTemplate.update(DELETE_GROUP, id);
    }

    @Override
    public List<Group> getAll() {
        log.info("Get all groups [Dao layer]");
        return jdbcTemplate.query(GET_ALL_GROUPS, groupRowMapper);
    }

    @Override
    public Group getById(Integer id) {
        log.info(String.format("Get group by id=%d [Dao layer]", id));
        List<Group> groups = jdbcTemplate.query(GET_GROUP_BY_ID, groupRowMapper, id);
        if (groups.isEmpty()) {
            return null;
        }
        return groups.get(0);
    }

    @Override
    public Group getByName(String name) {
        log.info("Get group by name=" + name + " [Dao layer]");
        List<Group> groups = jdbcTemplate.query(GET_GROUP_BY_NAME, groupRowMapper, name);
        if (groups.isEmpty()) {
            return null;
        }
        return groups.get(0);
    }

    @Override
    public void deleteStudentFromGroup(Student student) {
        log.info(String.format("Delete student (%s) from any group [Dao layer]", student.toString()));
        jdbcTemplate.update(DELETE_STUDENT_FROM_GROUP, student.getId());
    }

    @Override
    public void addStudentToGroup(Student student, Group group) {
        log.info(String.format("Add student (%s) to group (%s) [Dao layer]", student.toString(), group.toString()));
        jdbcTemplate.update(DELETE_STUDENT_FROM_GROUP, student.getId());
        jdbcTemplate.update(ADD_STUDENTS_TO_GROUP, student.getId(), group.getId());
    }

    @Override
    public void deleteCourseFromGroup(Course course, Group group) {
        log.info(String.format("Delete course (%s) from group (%s) [Dao layer]", course.toStringIdName(),
                group.toString()));
        jdbcTemplate.update(DELETE_COURSE_FROM_GROUP, course.getId(), group.getId());
    }

    @Override
    public void addCourseToGroup(Course course, Group group) {
        log.info(String.format("Add course (%s) to group (%s) [Dao layer]", course.toStringIdName(), group.toString()));
        jdbcTemplate.update(ADD_COURSES_TO_GROUP, course.getId(), group.getId());
    }

}
