package com.maistruk.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.maistruk.university.dao.StudentDao;
import com.maistruk.university.dao.jdbc.rowmapper.StudentRowMapper;
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
public class JdbcStudentDao implements StudentDao {

    static final String ADD_STUDENT = "INSERT INTO students (id, first_name, last_name) VALUES (DEFAULT, ?, ?)";
    static final String DELETE_STUDENT = "DELETE FROM students WHERE id = ?";
    static final String DELETE_GROUP_RELATED_TO_STUDENT = "DELETE FROM students_group WHERE student_id = ?";
    static final String UPDATE_STUDENT = "UPDATE students SET first_name = ?, last_name = ? WHERE id = ?";
    static final String GET_ALL_STUDENTS = "SELECT * FROM students";
    static final String GET_STUDENT_BY_ID = "SELECT * FROM students WHERE id = ?";
    static final String GET_STUDENT_BY_FULLNAME = "SELECT * FROM students WHERE first_name = ? AND last_name = ?";
    static final String GET_ALL_STUDENTS_BY_GROUP = "SELECT students.id, students.first_name, students.last_name"
            + " FROM students_group " + "LEFT JOIN students ON students_group.student_id = students.id "
            + "WHERE students_group.group_id = ?";
    static final String GET_STUDENT_BY_STUDENT_ID_AND_GROUP_ID = "SELECT students.id, students.first_name, students.last_name FROM students_group "
            + "LEFT JOIN students ON students_group.student_id = students.id "
            + "WHERE students_group.student_id = ? AND students_group.group_id = ?";

    JdbcTemplate jdbcTemplate;
    StudentRowMapper studentRowMapper;

    @Override
    public void create(Student student) {
        log.info(String.format("Create student (%s) [Dao layer]", student.toString()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(ADD_STUDENT, new String[] { "id" });
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            return statement;
        }, keyHolder);
        student.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void update(Student student) {
        log.info(String.format("Update student (%s) [Dao layer]", student.toString()));
        jdbcTemplate.update(UPDATE_STUDENT, student.getFirstName(), student.getLastName(), student.getId());
    }

    @Override
    public void delete(Integer id) {
        log.info(String.format("Delete student by id=%d [Dao layer]", id));
        jdbcTemplate.update(DELETE_GROUP_RELATED_TO_STUDENT, id);
        jdbcTemplate.update(DELETE_STUDENT, id);
    }

    @Override
    public List<Student> getAll() {
        log.info("Get all students [Dao layer]");
        return jdbcTemplate.query(GET_ALL_STUDENTS, studentRowMapper);
    }

    @Override
    public Student getById(Integer id) {
        log.info(String.format("Get student by id=%d [Dao layer]", id));
        List<Student> students = jdbcTemplate.query(GET_STUDENT_BY_ID, studentRowMapper, id);
        if (students.isEmpty()) {
            return null;
        }
        return students.get(0);
    }

    @Override
    public Student getByFullName(String firstName, String lastName) {
        log.info(String.format("Get student by full name (%s %s) [Dao layer]", firstName, lastName));
        List<Student> students = jdbcTemplate.query(GET_STUDENT_BY_FULLNAME, studentRowMapper, firstName, lastName);
        if (students.isEmpty()) {
            return null;
        }
        return students.get(0);
    }

    @Override
    public Student getByStudentAndGroup(Student student, Group group) {
        log.info(String.format("Check if student (%s) is in group (%s) [Dao layer]", student.toString(),
                group.toString()));
        List<Student> students = jdbcTemplate.query(GET_STUDENT_BY_STUDENT_ID_AND_GROUP_ID, studentRowMapper,
                student.getId(), group.getId());
        if (students.isEmpty()) {
            return null;
        }
        return students.get(0);
    }

    @Override
    public List<Student> getByGroup(Group group) {
        log.info(String.format("Get students by group (%s) [Dao layer]", group.toString()));
        return jdbcTemplate.query(GET_ALL_STUDENTS_BY_GROUP, studentRowMapper, group.getId());
    }

}
