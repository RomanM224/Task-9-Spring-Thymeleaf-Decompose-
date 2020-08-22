package com.maistruk.university.dao.jdbc;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.maistruk.university.config.TestConfig;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Student;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class JdbcGroupDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcGroupDao groupDao;

    @Test
    public void givenNewGroup_whenCreate_thenCreatedAndGetGeneratedId() {
        Group expectedGroup = Group.builder().id(4).name("AK-22").build();
        Group actualGroup = Group.builder().name("AK-22").build();

        groupDao.create(actualGroup);

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    public void givenExistingGroup_whenUpdate_thenUpdated() {
        Group expectedGroup = Group.builder().id(2).name("AK-22").build();

        groupDao.update(expectedGroup);

        Integer rowsInDatabaseExpected = 1;
        Integer rowsInDatabaseActual = countRowsInTableWhere(jdbcTemplate, "groups", "name = 'AK-22' and id = 2");
        assertEquals(rowsInDatabaseExpected, rowsInDatabaseActual);
    }

    @Test
    public void whenGetAll_thenGetAllGroups() {
        Group group1 = Group.builder().id(1).name("AK-27").build();
        Group group2 = Group.builder().id(2).name("FJ-52").build();
        Group group3 = Group.builder().id(3).name("AE-49").build();
        List<Group> expectedGroups = asList(group1, group2, group3);

        List<Group> actualGroups = groupDao.getAll();

        assertEquals(expectedGroups, actualGroups);
    }

    @Test
    public void givenGroupId_whenGetById_thenGetGroup() {
        Integer id = 2;
        Group expectedGroup = Group.builder().id(id).name("FJ-52").build();

        Group actualGroup = groupDao.getById(id);

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    public void givenGroupId_whenDelete_thenDeleted() {
        Integer rowsBeforeDelete = countRowsInTable(jdbcTemplate, "groups");

        groupDao.delete(2);

        Integer rowsAfterDelete = countRowsInTable(jdbcTemplate, "groups");
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

    @Test
    public void givenStudent_whenDeleteStudentFromGroup_thenDeleted() {
        Integer rowsBeforeDelete = countRowsInTable(jdbcTemplate, "students_group");
        Student student = Student.builder().id(2).build();

        groupDao.deleteStudentFromGroup(student);

        Integer rowsAfterDelete = countRowsInTable(jdbcTemplate, "students_group");
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

    @Test
    public void givenStudentAndGroup_whenAddStudentToGroup_thenAdded() {
        Integer rowsBeforeAdd = countRowsInTable(jdbcTemplate, "students_group WHERE group_id = 2");
        Student student = Student.builder().id(3).build();
        Group group = Group.builder().id(2).build();

        groupDao.addStudentToGroup(student, group);

        Integer rowsAfterAdd = countRowsInTable(jdbcTemplate, "students_group WHERE group_id = 2");
        assertEquals(rowsBeforeAdd + 1, rowsAfterAdd);
    }

    @Test
    public void givenCourseAndGroup_whenDeleteCourseFromGroup_thenDeleted() {
        Integer rowsBeforeDelete = countRowsInTable(jdbcTemplate, "courses_group WHERE group_id = 2");
        Course course = Course.builder().id(3).build();
        Group group = Group.builder().id(2).build();

        groupDao.deleteCourseFromGroup(course, group);

        Integer rowsAfterDelete = countRowsInTable(jdbcTemplate, "courses_group WHERE group_id = 2");
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

    @Test
    public void givenCourseAndGroup_whenAddCourseToGroup_thenAdded() {
        Integer rowsBeforeAdd = countRowsInTable(jdbcTemplate, "courses_group WHERE group_id = 2");
        Course course = Course.builder().id(1).build();
        Group group = Group.builder().id(2).build();

        groupDao.addCourseToGroup(course, group);

        Integer rowsAfterAdd = countRowsInTable(jdbcTemplate, "courses_group WHERE group_id = 2");
        assertEquals(rowsBeforeAdd + 1, rowsAfterAdd);
    }
}
