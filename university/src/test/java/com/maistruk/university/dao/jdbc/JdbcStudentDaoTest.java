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
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Student;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class JdbcStudentDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcStudentDao studentDao;

    @Test
    public void givenNewStudent_whenCreate_thenCreatedAndGetGeneratedId() {
        Student expectedStudent = Student.builder().id(11).firstName("Jemmy").lastName("Carr").build();
        Student actualStudent = Student.builder().firstName("Jemmy").lastName("Carr").build();

        studentDao.create(actualStudent);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    public void givenExistingStudent_whenUpdate_thenUpdated() {
        Student expectedStudent = Student.builder().id(3).firstName("Name").lastName("Surname").build();

        studentDao.update(expectedStudent);

        Integer rowsInDatabaseExpected = 1;
        Integer rowsInDatabaseActual = countRowsInTableWhere(jdbcTemplate, "students",
                "first_name = 'Name' and last_name = 'Surname' and id = 3");
        assertEquals(rowsInDatabaseExpected, rowsInDatabaseActual);
    }

    @Test
    public void whenGetAll_thenGetAllStudents() {
        Student student1 = Student.builder().id(1).firstName("Brianne").lastName("Wetterlund").build();
        Student student2 = Student.builder().id(2).firstName("Kate").lastName("Watson").build();
        Student student3 = Student.builder().id(3).firstName("Josh").lastName("Lawrence").build();
        Student student4 = Student.builder().id(4).firstName("Kate").lastName("Ross").build();
        Student student5 = Student.builder().id(5).firstName("Sophie").lastName("Middleditch").build();
        Student student6 = Student.builder().id(6).firstName("Matt").lastName("Brener").build();
        Student student7 = Student.builder().id(7).firstName("Martin").lastName("Shipp").build();
        Student student8 = Student.builder().id(8).firstName("Alice").lastName("Nanjiani").build();
        Student student9 = Student.builder().id(9).firstName("Emily").lastName("Connelly").build();
        Student student10 = Student.builder().id(10).firstName("Leonardo").lastName("Miller").build();
        List<Student> expectedStudents = asList(student1, student2, student3, student4, student5, student6, student7,
                student8, student9, student10);

        List<Student> actualStudents = studentDao.getAll();

        assertEquals(expectedStudents, actualStudents);
    }

    @Test
    public void givenStudentId_whenGetById_thenGetStudent() {
        Integer id = 3;
        Student expectedStudent = Student.builder().id(id).firstName("Josh").lastName("Lawrence").build();

        Student actualStudent = studentDao.getById(id);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    public void givenStudentId_whenDelete_thenDeleted() {
        Integer rowsBeforeDelete = countRowsInTable(jdbcTemplate, "students");

        studentDao.delete(2);

        Integer rowsAfterDelete = countRowsInTable(jdbcTemplate, "students");
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

    @Test
    public void givenGroup_whenGetByGroup_thenGetStudents() {
        Group group = Group.builder().id(3).build();
        Student student1 = Student.builder().id(3).firstName("Josh").lastName("Lawrence").build();
        Student student2 = Student.builder().id(6).firstName("Matt").lastName("Brener").build();
        Student student3 = Student.builder().id(9).firstName("Emily").lastName("Connelly").build();
        List<Student> expectedStudents = asList(student1, student2, student3);

        List<Student> actualStudents = studentDao.getByGroup(group);

        assertEquals(expectedStudents, actualStudents);
    }

}
