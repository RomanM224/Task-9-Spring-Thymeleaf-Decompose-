package com.maistruk.university.controller;

import static com.maistruk.university.controller.CourseControllerTest.TestData.course;
import static com.maistruk.university.controller.CourseControllerTest.TestData.course2;
import static com.maistruk.university.controller.CourseControllerTest.TestData.courseDto;
import static com.maistruk.university.controller.CourseControllerTest.TestData.courseDto2;
import static com.maistruk.university.controller.CourseControllerTest.TestData.courseId2;
import static com.maistruk.university.controller.CourseControllerTest.TestData.courses;
import static com.maistruk.university.controller.CourseControllerTest.TestData.courses2;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import com.maistruk.university.exceptions.CourseException;
import com.maistruk.university.mapper.CourseMapper;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.dto.CourseDto;
import com.maistruk.university.service.CourseService;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = TestConfig.class)
public class CourseControllerTest {

    @Mock
    private CourseService courseService;
    @Mock
    private View mockView;
    @Mock
    private CourseMapper courseMapper;
    @InjectMocks
    private CourseController courseController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(courseController).setSingleView(mockView).build();
    }

    @Test
    public void givenShowAllURI_whenGetAll_thenSuccess() throws Exception {
        when(courseService.getAll()).thenReturn(courses);
        mockMvc.perform(get("/course/showAll"))
               .andExpect(model().attribute("courses", courses))
               .andExpect(status().isOk())
               .andExpect(view().name("/course/showAll"));
    }


    @Test
    public void givenGetByIdURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/course/getById"))
               .andExpect(status().isOk())
               .andExpect(view().name("/course/getById"));
    }
    
    @Test
    public void givenCourseId_whenGetById_thenSuccess() throws Exception {
        when(courseService.getById(2)).thenReturn(course2);
        mockMvc.perform(post("/course/getById").param("id", "2"))
               .andExpect(model().attribute("courses", courses2))
               .andExpect(status().isOk())
               .andExpect(view().name("/course/showAll"));
    }
    
    @Test
    public void givenCreateURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/course/create"))
               .andExpect(status().isOk())
               .andExpect(view().name("/course/create"));
    }
    
    @Test
    public void givenCourse_whenCreate_thenSuccess()  throws Exception{
        when(courseMapper.mapCourseDtoToCourse(courseDto)).thenReturn(course);
        mockMvc.perform(post("/course/create").param("name", "Database").param("description", "Database description"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(courseService).create(course);
    }
    
    @Test
    public void givenWrongCourse_whenCreate_thenThrowException()  throws Exception{
        when(courseMapper.mapCourseDtoToCourse(courseDto)).thenReturn(course);
        doThrow(CourseException.class).when(courseService).create(course);
        mockMvc.perform(post("/course/create").param("name", "Database").param("description", "Database description"))
               .andExpect(status().isOk())
               .andExpect(view().name("/course/create"));
    }
    
    @Test
    public void givenUpdateURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/course/update"))
               .andExpect(status().isOk())
               .andExpect(view().name("/course/update"));
    }
    
    @Test
    public void givenCourse_whenUpdate_thenSuccess()  throws Exception{
        when(courseMapper.mapCourseDtoToCourse(courseDto2)).thenReturn(course2);
        mockMvc.perform(post("/course/update").param("id", "2").param("name", "Computer science")
               .param("description", "Computer science description"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(courseService).update(course2);
    }
    
    @Test
    public void givenWrongCourse_whenUpdate_thenThrowException()  throws Exception{
        when(courseMapper.mapCourseDtoToCourse(courseDto2)).thenReturn(course2);
        doThrow(CourseException.class).when(courseService).update(course2);
        mockMvc.perform(post("/course/update").param("id", "2").param("name", "Computer science")
               .param("description", "Computer science description"))
               .andExpect(status().isOk())
               .andExpect(view().name("/course/update"));
    }
    
    @Test
    public void givenDeleteURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/course/delete"))
               .andExpect(status().isOk())
               .andExpect(view().name("/course/delete"));
    }
    
    @Test
    public void givenCourseId_whenDelete_thenSuccess()  throws Exception{
        mockMvc.perform(post("/course/delete").param("id", "2"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(courseService).delete(courseId2);
    }
    
    @Test
    public void givenWrongCourseId_whenDelete_thenThrowException()  throws Exception{
        doThrow(CourseException.class).when(courseService).delete(3);
        mockMvc.perform(post("/course/delete").param("id", "3"))
               .andExpect(status().isOk())
               .andExpect(view().name("/course/delete"));
    }
    
    interface TestData {

        Integer courseId = 1;
        Integer courseId2 = 2;
        Integer courseId3 = 3;

        String courseName = "Database";
        String courseName2 = "Computer science";
        String courseName3 = "Geography";

        Course course = Course.builder().name(courseName).description("Database description").build();
        Course course2 = Course.builder().id(courseId2).name(courseName2).description("Computer science description")
                .build();
        Course course3 = Course.builder().id(courseId3).name(courseName3).description("Geography description").build();
        List<Course> courses = asList(course, course2, course3);
        List<Course> courses2 = asList(course2);
        
        CourseDto courseDto = CourseDto.builder().name(courseName).description("Database description").build();
        CourseDto courseDto2 = CourseDto.builder().id(courseId2).name(courseName2).description("Computer science description").build();

    }
}
