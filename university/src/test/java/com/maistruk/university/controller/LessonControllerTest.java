package com.maistruk.university.controller;

import static com.maistruk.university.controller.LessonControllerTest.TestData.auditories;
import static com.maistruk.university.controller.LessonControllerTest.TestData.courses;
import static com.maistruk.university.controller.LessonControllerTest.TestData.date;
import static com.maistruk.university.controller.LessonControllerTest.TestData.emptyDate;
import static com.maistruk.university.controller.LessonControllerTest.TestData.group;
import static com.maistruk.university.controller.LessonControllerTest.TestData.groups;
import static com.maistruk.university.controller.LessonControllerTest.TestData.lesson;
import static com.maistruk.university.controller.LessonControllerTest.TestData.lesson2;
import static com.maistruk.university.controller.LessonControllerTest.TestData.lesson4;
import static com.maistruk.university.controller.LessonControllerTest.TestData.lessonDto;
import static com.maistruk.university.controller.LessonControllerTest.TestData.lessonDto2;
import static com.maistruk.university.controller.LessonControllerTest.TestData.lessons;
import static com.maistruk.university.controller.LessonControllerTest.TestData.lessons2;
import static com.maistruk.university.controller.LessonControllerTest.TestData.teacher;
import static com.maistruk.university.controller.LessonControllerTest.TestData.teachers;
import static com.maistruk.university.controller.LessonControllerTest.TestData.timeTables;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;

import com.maistruk.university.config.TestConfig;
import com.maistruk.university.exceptions.LessonException;
import com.maistruk.university.mapper.LessonMapper;
import com.maistruk.university.model.Auditory;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Lesson;
import com.maistruk.university.model.Teacher;
import com.maistruk.university.model.TimeTable;
import com.maistruk.university.model.dto.LessonDto;
import com.maistruk.university.service.AuditoryService;
import com.maistruk.university.service.CourseService;
import com.maistruk.university.service.GroupService;
import com.maistruk.university.service.LessonService;
import com.maistruk.university.service.TeacherService;
import com.maistruk.university.service.TimeTableService;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = TestConfig.class)
public class LessonControllerTest {

    @Mock
    private GroupService groupService;
    @Mock
    private CourseService courseService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private AuditoryService auditoryService;
    @Mock
    private TimeTableService timeTableService;
    @Mock
    private LessonService lessonService;
    @Mock
    private View mockView;
    @Mock
    private LessonMapper lessonMapper;
    @InjectMocks
    private LessonController lessonController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(lessonController).setSingleView(mockView).build();
    }

    @Test
    public void givenShowAllURI_whenGetAll_thenSuccess() throws Exception {
        when(lessonService.getAll()).thenReturn(lessons);
        mockMvc.perform(get("/lesson/showAll")).andExpect(status().isOk())
               .andExpect(model().attribute("lessons", lessons))
               .andExpect(view().name("/lesson/showAll"));
    }
    
    @Test
    public void givenGetByIdURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/lesson/getById"))
               .andExpect(status().isOk())
               .andExpect(view().name("/lesson/getById"));
    }
    
    @Test
    public void givenLessonId_whenGetById_thenSuccess() throws Exception {
        when(lessonService.getById(4)).thenReturn(lesson4);
        mockMvc.perform(post("/lesson/getById").param("id", "4"))
               .andExpect(model().attribute("lessons", lessons2))
               .andExpect(status().isOk())
               .andExpect(view().name("/lesson/showAll"));
    }
    
    @Test
    public void givenCreateURI_whenGetAllModels_thenSuccess() throws Exception {
        when(groupService.getAll()).thenReturn(groups);
        when(courseService.getAll()).thenReturn(courses);
        when(teacherService.getAll()).thenReturn(teachers);
        when(auditoryService.getAll()).thenReturn(auditories);
        when(timeTableService.getAll()).thenReturn(timeTables);
        mockMvc.perform(get("/lesson/create"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("groups", groups))
               .andExpect(model().attribute("courses", courses))
               .andExpect(model().attribute("teachers", teachers))
               .andExpect(model().attribute("auditories", auditories))
               .andExpect(model().attribute("timeTables", timeTables))
               .andExpect(view().name("/lesson/create"));
    }
    
    @Test
    public void givenLesson_whenCreate_thenSuccess()  throws Exception{
        when(lessonMapper.mapLessonDtoToLesson(lessonDto)).thenReturn(lesson);
        mockMvc.perform(post("/lesson/create").param("groupId", "1").param("courseId", "1")
               .param("teacherId", "1").param("auditoryId", "1").param("timeTableId", "1")
               .param("date", "2020-09-01"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(lessonService).create(lesson);
    }
    
    @Test
    public void givenWrongLesson_whenCreate_thenThrowException()  throws Exception{
        when(lessonMapper.mapLessonDtoToLesson(lessonDto)).thenReturn(lesson);
        doThrow(LessonException.class).when(lessonService).create(lesson);
        mockMvc.perform(post("/lesson/create").param("groupId", "1").param("courseId", "1")
               .param("teacherId", "1").param("auditoryId", "1").param("timeTableId", "1")
               .param("date", "2020-09-01"))
               .andExpect(status().isOk())
               .andExpect(view().name("/lesson/create"));
    }
    
    @Test
    public void givenUpdateURI_whenGetAllModels_thenSuccess() throws Exception {
        when(lessonService.getAll()).thenReturn(lessons);
        when(groupService.getAll()).thenReturn(groups);
        when(courseService.getAll()).thenReturn(courses);
        when(teacherService.getAll()).thenReturn(teachers);
        when(auditoryService.getAll()).thenReturn(auditories);
        when(timeTableService.getAll()).thenReturn(timeTables);
        mockMvc.perform(get("/lesson/update"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("lessons", lessons))
               .andExpect(model().attribute("groups", groups))
               .andExpect(model().attribute("courses", courses))
               .andExpect(model().attribute("teachers", teachers))
               .andExpect(model().attribute("auditories", auditories))
               .andExpect(model().attribute("timeTables", timeTables))
               .andExpect(view().name("/lesson/update"));
    }
    
    @Test
    public void givenLesson_whenUpdate_thenSuccess()  throws Exception{
        when(lessonMapper.mapLessonDtoToLesson(lessonDto2)).thenReturn(lesson2);
        mockMvc.perform(post("/lesson/update").param("lessonId", "2").param("groupId", "1").param("courseId", "1")
               .param("teacherId", "1").param("auditoryId", "1").param("timeTableId", "1")
               .param("date", "2020-09-01"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(lessonService).update(lesson2);
    }
    
    @Test
    public void givenWrongLesson_whenUpdate_thenThrowException()  throws Exception{
        when(lessonMapper.mapLessonDtoToLesson(lessonDto2)).thenReturn(lesson2);
        doThrow(LessonException.class).when(lessonService).update(lesson2);
        mockMvc.perform(post("/lesson/update").param("lessonId", "2").param("groupId", "1").param("courseId", "1")
               .param("teacherId", "1").param("auditoryId", "1").param("timeTableId", "1")
               .param("date", "2020-09-01"))
               .andExpect(status().isOk())
               .andExpect(view().name("/lesson/update"));
    }
    
    @Test
    public void givenDeleteURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/lesson/delete"))
               .andExpect(status().isOk())
               .andExpect(view().name("/lesson/delete"));
    }
    
    @Test
    public void givenLessonId_whenDelete_thenSuccess()  throws Exception{
        mockMvc.perform(post("/lesson/delete").param("id", "3"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(lessonService).delete(3);
    }
    
    @Test
    public void givenWrongLessonId_whenDelete_thenThrowException()  throws Exception{
        doThrow(LessonException.class).when(lessonService).delete(7);
        mockMvc.perform(post("/lesson/delete").param("id", "7"))
               .andExpect(status().isOk())
               .andExpect(view().name("/lesson/delete"));
    }
    
    @Test
    public void givenShowByGroupURI_whenGetAllGroups_thenSuccess() throws Exception {
        when(groupService.getAll()).thenReturn(groups);
        mockMvc.perform(get("/lesson/showByGroup"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("groups", groups))
               .andExpect(view().name("/lesson/showByGroup"));
    }
    
    @Test
    public void givenGroupStartTimeFinishTime_whenGetByGroup_thenSuccess() throws Exception {
        mockMvc.perform(post("/lesson/showByGroup").param("id", "1").param("startDate", "2020-09-01")
               .param("finishDate", "2020-09-01"))
               .andExpect(status().isOk())
               .andExpect(view().name("/lesson/showAll"));
        verify(lessonService).getByGroup(group, date, date);
    }
    
    @Test
    public void givenGroupAndEmptyStartTimeFinishTime_whenGetByGroup_thenThrowException() throws Exception{
        when(lessonService.getByGroup(group, emptyDate, emptyDate)).thenThrow(LessonException.class);
        mockMvc.perform(post("/lesson/showByGroup").param("id", "1").param("startDate", "")
                .param("finishDate", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("/lesson/showByGroup"));
    }
    
    @Test
    public void givenShowByTeacherURI_whenGetAllTeachers_thenSuccess() throws Exception {
        when(teacherService.getAll()).thenReturn(teachers);
        mockMvc.perform(get("/lesson/showByTeacher"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("teachers", teachers))
               .andExpect(view().name("/lesson/showByTeacher"));
    }
    
    @Test
    public void givenTeacherStartTimeFinishTime_whenGetByTeacher_thenSuccess() throws Exception {
        mockMvc.perform(post("/lesson/showByTeacher").param("id", "1").param("startDate", "2020-09-01")
               .param("finishDate", "2020-09-01"))
               .andExpect(status().isOk())
               .andExpect(view().name("/lesson/showAll"));
        verify(lessonService).getByTeacher(teacher, date, date);
    }
    
    @Test
    public void givenTeacherAndEmptyStartTimeFinishTime_whenGetByTeacher_thenThrowException() throws Exception{
        when(lessonService.getByTeacher(teacher, emptyDate, emptyDate)).thenThrow(LessonException.class);
        mockMvc.perform(post("/lesson/showByTeacher").param("id", "1").param("startDate", "")
                .param("finishDate", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("/lesson/showByTeacher"));
    }

    interface TestData {
        Integer groupId = 1;
        Integer courseId = 1;
        Integer teacherId = 1;
        Integer auditoryId = 1;
        Integer timeTableId = 1;
        
        LocalDate date = LocalDate.parse("2020-09-01");
        LocalDate date2 = LocalDate.parse("2020-09-02");
        LocalDate date3 = LocalDate.parse("2020-07-03");
        LocalDate date4 = LocalDate.parse("2020-08-03");
        LocalDate emptyDate = null;
        Group group = Group.builder().id(groupId).build();
        Group group2 = Group.builder().id(2).name("FJ-52").build();
        Group group3 = Group.builder().id(3).name("AE-49").build();
        List<Group> groups = asList(group);
        Course course = Course.builder().id(courseId).build();
        Course course2 = Course.builder().id(2).name("Computer science").description(
                "Computer science is the study of processes that interact with data and that can be represented as data in the form of programs.")
                .build();
        Course course3 = Course.builder().id(3).name("Geography").description(
                "Geography is a field of science devoted to the study of the lands, features, inhabitants, and phenomena of the Earth and planets.")
                .build();
        List<Course> courses = asList(course);
        Teacher teacher = Teacher.builder().id(teacherId).build();
        Teacher teacher2 = Teacher.builder().id(2).firstName("Peter").lastName("Russo").build();
        Teacher teacher3 = Teacher.builder().id(3).firstName("Lucas").lastName("Goodwin").build();
        List<Teacher> teachers = asList(teacher);
        Auditory auditory = Auditory.builder().id(auditoryId).build();
        Auditory auditory2 = Auditory.builder().id(2).number(101).build();
        Auditory auditory3 = Auditory.builder().id(3).number(102).build();
        List<Auditory> auditories = asList(auditory);
        TimeTable timeTable = TimeTable.builder().id(timeTableId).build();
        TimeTable timeTable2 = TimeTable.builder().id(2).startTime(LocalTime.parse("10:40"))
                .finishTime(LocalTime.parse("12:00")).build();
        TimeTable timeTable3 = TimeTable.builder().id(3).startTime(LocalTime.parse("12:20"))
                .finishTime(LocalTime.parse("13:40")).build();
        TimeTable timeTable4 = TimeTable.builder().id(4).startTime(LocalTime.parse("14:00"))
                .finishTime(LocalTime.parse("15:20")).build();
        List<TimeTable> timeTables = asList(timeTable);

        Lesson lesson = Lesson.builder().group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        Lesson lesson2 = Lesson.builder().id(2).group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        Lesson lesson3 = Lesson.builder().id(3).group(group2).course(course3).teacher(teacher3).auditory(auditory3)
                .timeTable(timeTable2).date(date2).build();
        Lesson lesson4 = Lesson.builder().id(4).group(group2).course(course2).teacher(teacher2).auditory(auditory3)
                .timeTable(timeTable3).date(date2).build();
        Lesson lesson5 = Lesson.builder().id(5).group(group3).course(course2).teacher(teacher2).auditory(auditory2)
                .timeTable(timeTable4).date(date3).build();
        Lesson lesson6 = Lesson.builder().id(6).group(group3).course(course3).teacher(teacher3).auditory(auditory2)
                .timeTable(timeTable2).date(date4).build();
        List<Lesson> lessons = asList(lesson3, lesson4, lesson5, lesson6);
        List<Lesson> lessons2 = asList(lesson4);
        
        LessonDto lessonDto = LessonDto.builder().groupId(groupId).courseId(courseId).teacherId(teacherId)
                .auditoryId(auditoryId).timeTableId(timeTableId).date(date).build();
        LessonDto lessonDto2 = LessonDto.builder().lessonId(2).groupId(groupId).courseId(courseId).teacherId(teacherId)
                .auditoryId(auditoryId).timeTableId(timeTableId).date(date).build();

    }
}
