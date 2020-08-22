package com.maistruk.university.service;

import static com.maistruk.university.service.StudentServiceTest.TestData.firstName;
import static com.maistruk.university.service.StudentServiceTest.TestData.lastName;
import static com.maistruk.university.service.StudentServiceTest.TestData.notExiStudent;
import static com.maistruk.university.service.StudentServiceTest.TestData.student;
import static com.maistruk.university.service.StudentServiceTest.TestData.student2;
import static com.maistruk.university.service.StudentServiceTest.TestData.studentId;
import static com.maistruk.university.service.StudentServiceTest.TestData.students;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.maistruk.university.dao.jdbc.JdbcStudentDao;
import com.maistruk.university.exceptions.StudentException;
import com.maistruk.university.model.Student;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private JdbcStudentDao studentDao;
    @InjectMocks
    private StudentService studentService;

    @Test
    public void givenStudent_whenCreate_thenCreated() throws StudentException {
        when(studentDao.getByFullName(firstName, lastName)).thenReturn(notExiStudent);

        studentService.create(student);

        verify(studentDao).create(student);
    }

    @Test
    public void givenStudentWithExistName_whenCreate_thenNotCreated() {
        when(studentDao.getByFullName(firstName, lastName)).thenReturn(student);

        assertThrows(StudentException.class, () -> studentService.create(student));
    }

    @Test
    public void givenStudent_whenUpdate_thenUpdated() throws StudentException {
        when(studentDao.getById(studentId)).thenReturn(student);
        when(studentDao.getByFullName(firstName, lastName)).thenReturn(notExiStudent);

        studentService.update(student);

        verify(studentDao).update(student);
    }

    @Test
    public void givenNotExistStudent_whenUpdate_thenNotUpdated() throws StudentException {
        when(studentDao.getById(studentId)).thenReturn(notExiStudent);

        assertThrows(StudentException.class, () -> studentService.update(student));
    }

    @Test
    public void givenStudentWithExistName_whenUpdate_thenNotUpdated() throws StudentException {
        when(studentDao.getById(studentId)).thenReturn(student);
        when(studentDao.getByFullName(firstName, lastName)).thenReturn(student);

        assertThrows(StudentException.class, () -> studentService.update(student));
    }

    @Test
    public void whenGetAll_thenGetAllStudents() {
        when(studentDao.getAll()).thenReturn(students);

        List<Student> actualStudents = studentService.getAll();

        assertEquals(students, actualStudents);
    }

    @Test
    public void givenStudntId_whenGetById_thenGetStudent() {
        when(studentDao.getById(2)).thenReturn(student2);

        Student actualStudent = studentService.getById(2);

        assertEquals(student2, actualStudent);
    }

    interface TestData {

        Integer studentId = 1;
        Integer studentId2 = 2;
        Integer studentId3 = 3;

        String firstName = "Brianne";
        String lastName = "Wetterlund";

        Student student = Student.builder().id(1).firstName(firstName).lastName(lastName).build();
        Student student2 = Student.builder().id(2).firstName("Kate").lastName("Watson").build();
        Student student3 = Student.builder().id(3).firstName("Josh").lastName("Lawrence").build();
        Student notExiStudent = null;

        List<Student> students = asList(student, student2, student3);

    }

}
