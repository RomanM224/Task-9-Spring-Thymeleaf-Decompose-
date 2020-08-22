package com.maistruk.university.service;

import static com.maistruk.university.service.GroupServiceTest.TestData.course;
import static com.maistruk.university.service.GroupServiceTest.TestData.course2;
import static com.maistruk.university.service.GroupServiceTest.TestData.group;
import static com.maistruk.university.service.GroupServiceTest.TestData.groupCourses;
import static com.maistruk.university.service.GroupServiceTest.TestData.groupId;
import static com.maistruk.university.service.GroupServiceTest.TestData.groupName;
import static com.maistruk.university.service.GroupServiceTest.TestData.groupStudents;
import static com.maistruk.university.service.GroupServiceTest.TestData.groups;
import static com.maistruk.university.service.GroupServiceTest.TestData.notExistCourse;
import static com.maistruk.university.service.GroupServiceTest.TestData.notExistGroup;
import static com.maistruk.university.service.GroupServiceTest.TestData.notExistStudent;
import static com.maistruk.university.service.GroupServiceTest.TestData.student;
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

import com.maistruk.university.dao.jdbc.JdbcGroupDao;
import com.maistruk.university.exceptions.GroupException;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Student;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock
    private StudentService studentService;
    @Mock
    private CourseService courseService;
    @Mock
    private JdbcGroupDao groupDao;
    @InjectMocks
    private GroupService groupService;

    @Test
    public void givenGroup_whenCreate_thenCreated() throws GroupException {
        when(groupDao.getByName(groupName)).thenReturn(notExistGroup);

        groupService.create(group);

        verify(groupDao).create(group);
    }

    @Test
    public void givenGroupWithExistName_whenCreate_thenNotCreated() {
        when(groupDao.getByName(groupName)).thenReturn(group);

        assertThrows(GroupException.class, () -> groupService.create(group));
    }

    @Test
    public void givenGroup_whenUpdate_thenUpdated() throws GroupException {
        when(groupDao.getById(groupId)).thenReturn(group);
        when(groupDao.getByName(groupName)).thenReturn(notExistGroup);

        groupService.update(group);
        verify(groupDao).update(group);
    }

    @Test
    public void givenNotExistGroup_whenUpdate_thenNotUpdated() {
        when(groupDao.getById(groupId)).thenReturn(notExistGroup);

        assertThrows(GroupException.class, () -> groupService.update(group));
    }

    @Test
    public void givenGroupWithExistName_whenUpdate_thenNotUpdated() {
        when(groupDao.getById(groupId)).thenReturn(group);
        when(groupDao.getByName(groupName)).thenReturn(group);

        assertThrows(GroupException.class, () -> groupService.update(group));
    }

    @Test
    public void whenGetAll_thenGetAllGroups() {
        when(groupDao.getAll()).thenReturn(groups);

        List<Group> actualGroups = groupService.getAll();

        assertEquals(groups, actualGroups);
    }

    @Test
    public void givenGroupId_whenGetById_thenGetGroup() {
        when(groupDao.getById(groupId)).thenReturn(group);

        Group actualGroup = groupService.getById(groupId);

        assertEquals(group, actualGroup);
    }

    @Test
    public void givenGroup_whenGetStudentsByGroup_thenGetStudents() {
        when(studentService.getByGroup(group)).thenReturn(groupStudents);

        List<Student> actualStudents = groupService.getStudentsByGroup(group);

        assertEquals(groupStudents, actualStudents);
    }

    @Test
    public void givenGroup_whenGetCoursesByGroup_thenGetCourses() {
        when(courseService.getByGroup(group)).thenReturn(groupCourses);

        List<Course> actualCourses = groupService.getCoursesByGroup(group);

        assertEquals(groupCourses, actualCourses);
    }

    @Test
    public void givenStudentAndGroup_whenAddStudentToGroup_thenAdded() throws GroupException {
        when(studentService.getByStudentAndGroup(student, group)).thenReturn(notExistStudent);

        groupService.addStudentToGroup(student, group);

        verify(groupDao).addStudentToGroup(student, group);
    }

    @Test
    public void givenNotExistingStudentAndExistingGroup_whenAddStudentToGroup_thenNotAdded() {

        assertThrows(GroupException.class, () -> groupService.addStudentToGroup(notExistStudent, group));
    }

    @Test
    public void givenExistingStudentAndNotExistingGroup_whenAddStudentToGroup_thenNotAdded() {

        assertThrows(GroupException.class, () -> groupService.addStudentToGroup(student, notExistGroup));
    }

    @Test
    public void givenCourseAndGroup_whenDeleteCourseFromGroup_thenDeleted() throws GroupException {
        when(courseService.getByGroup(group)).thenReturn(groupCourses);

        groupService.deleteCourseFromGroup(course, group);

        verify(groupDao).deleteCourseFromGroup(course, group);
    }

    @Test
    public void givenNotExistingCourseAndExistingGroup_whenDeleteCourseFromGroup_thenNotDeleted() {

        assertThrows(GroupException.class, () -> groupService.deleteCourseFromGroup(notExistCourse, group));
    }

    @Test
    public void givenExistingCourseAndNotExistingGroup_whenDeleteCourseFromGroup_thenNotDeleted() {

        assertThrows(GroupException.class, () -> groupService.deleteCourseFromGroup(course, notExistGroup));
    }

    @Test
    public void givenCourseAndGroup_whenAddCourseToGroup_thenAdded() throws GroupException {
        when(courseService.getByGroup(group)).thenReturn(groupCourses);

        groupService.addCourseToGroup(course2, group);

        verify(groupDao).addCourseToGroup(course2, group);
    }

    @Test
    public void givenNotExistingCourseAndExistingGroup_whenAddCourseToGroup_thenNotAdded() {

        assertThrows(GroupException.class, () -> groupService.addCourseToGroup(notExistCourse, group));
    }

    @Test
    public void givenExistingCourseAndNotExistingGroup_whenAddCourseToGroup_thenNotAdded() {

        assertThrows(GroupException.class, () -> groupService.addCourseToGroup(course, notExistGroup));
    }

    interface TestData {

        Integer groupId = 1;
        Integer groupId2 = 2;
        Integer groupId3 = 3;
        Integer studetnId = 5;
        Integer studetnId2 = 6;
        Integer courseId = 7;
        Integer courseId2 = 8;
        Integer worngId = 55;

        String groupName = "AK-27";
        String groupName2 = "FJ-52";
        String groupName3 = "AE-49";

        Group group = Group.builder().id(groupId).name(groupName).build();
        Group group2 = Group.builder().id(groupId2).name(groupName2).build();
        Group group3 = Group.builder().id(groupId3).name(groupName3).build();
        Group notExistGroup = null;
        List<Group> groups = asList(group, group2, group3);

        Student student = Student.builder().id(studetnId).firstName("Brianne").lastName("Wetterlund").build();
        Student student2 = Student.builder().id(studetnId2).firstName("Kate").lastName("Watson").build();
        Student notExistStudent = null;
        List<Student> groupStudents = asList(student, student2);

        Course course = Course.builder().id(courseId).name("Database").description(
                "Database is the study of an organized collection of data, generally stored and accessed electronically from a computer system.")
                .build();
        Course course2 = Course.builder().id(courseId2).name("Computer science").description(
                "Computer science is the study of processes that interact with data and that can be represented as data in the form of programs.")
                .build();
        Course notExistCourse = null;
        List<Course> groupCourses = asList(course);

    }

}
