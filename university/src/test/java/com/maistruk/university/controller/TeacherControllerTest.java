package com.maistruk.university.controller;

import static com.maistruk.university.controller.TeacherControllerTest.TestData.teacher;
import static com.maistruk.university.controller.TeacherControllerTest.TestData.teacher2;
import static com.maistruk.university.controller.TeacherControllerTest.TestData.teacher3;
import static com.maistruk.university.controller.TeacherControllerTest.TestData.teacherDto;
import static com.maistruk.university.controller.TeacherControllerTest.TestData.teacherDto2;
import static com.maistruk.university.controller.TeacherControllerTest.TestData.teachers;
import static com.maistruk.university.controller.TeacherControllerTest.TestData.teachers2;
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
import com.maistruk.university.exceptions.TeacherException;
import com.maistruk.university.mapper.TeacherMapper;
import com.maistruk.university.model.Teacher;
import com.maistruk.university.model.dto.TeacherDto;
import com.maistruk.university.service.TeacherService;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = TestConfig.class)
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;
    @Mock
    private View mockView;
    @Mock
    private TeacherMapper teacherMapper;
    @InjectMocks
    private TeacherController teacherController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(teacherController).setSingleView(mockView).build();
    }

    @Test
    public void givenShowAllURI_whenGetAll_thenSuccess() throws Exception {
        when(teacherService.getAll()).thenReturn(teachers);
        mockMvc.perform(get("/teacher/showAll"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("teachers", teachers))
               .andExpect(view().name("/teacher/showAll"));
    }
    
    @Test
    public void givenGetByIdURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/teacher/getById"))
               .andExpect(status().isOk())
               .andExpect(view().name("/teacher/getById"));
    }
    
    @Test
    public void givenTeacherId_whenGetById_thenSuccess() throws Exception {
        when(teacherService.getById(3)).thenReturn(teacher3);
        mockMvc.perform(post("/teacher/getById").param("id", "3"))
               .andExpect(model().attribute("teachers", teachers2))
               .andExpect(status().isOk())
               .andExpect(view().name("/teacher/showAll"));
    }
    
    @Test
    public void givenCreateURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/teacher/create"))
               .andExpect(status().isOk())
               .andExpect(view().name("/teacher/create"));
    }
    
    @Test
    public void givenTeacher_whenCreate_thenSuccess()  throws Exception{
        when(teacherMapper.mapTeacherDtoToTeacher(teacherDto)).thenReturn(teacher);
        mockMvc.perform(post("/teacher/create").param("firstName", "Zoe").param("lastName", "Barnes"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(teacherService).create(teacher);
    }
    
    @Test
    public void givenWrongTeacher_whenCreate_thenThrowException()  throws Exception{
        when(teacherMapper.mapTeacherDtoToTeacher(teacherDto)).thenReturn(teacher);
        doThrow(TeacherException.class).when(teacherService).create(teacher);
        mockMvc.perform(post("/teacher/create").param("firstName", "Zoe").param("lastName", "Barnes"))
               .andExpect(status().isOk())
               .andExpect(view().name("/teacher/create"));
    }
    
    @Test
    public void givenUpdateURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/teacher/update"))
               .andExpect(status().isOk())
               .andExpect(view().name("/teacher/update"));
    }
    
    @Test
    public void givenTeacher_whenUpdate_thenSuccess()  throws Exception{
        when(teacherMapper.mapTeacherDtoToTeacher(teacherDto2)).thenReturn(teacher2);
        mockMvc.perform(post("/teacher/update").param("id", "2").param("firstName", "Peter").param("lastName", "Russo"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(teacherService).update(teacher2);
    }
    
    @Test
    public void givenWrongTeacher_whenUpdate_thenThrowException()  throws Exception{
        when(teacherMapper.mapTeacherDtoToTeacher(teacherDto2)).thenReturn(teacher2);
        doThrow(TeacherException.class).when(teacherService).update(teacher2);
        mockMvc.perform(post("/teacher/update").param("id", "2").param("firstName", "Peter").param("lastName", "Russo"))
               .andExpect(status().isOk())
               .andExpect(view().name("/teacher/update"));
    }
    
    @Test
    public void givenDeleteURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/teacher/delete"))
               .andExpect(status().isOk())
               .andExpect(view().name("/teacher/delete"));
    }
    
    @Test
    public void givenTeacherId_whenDelete_thenSuccess()  throws Exception{
        mockMvc.perform(post("/teacher/delete").param("id", "3"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(teacherService).delete(3);
    }
    
    @Test
    public void givenWrongTeacherId_whenDelete_thenThrowException()  throws Exception{
        doThrow(TeacherException.class).when(teacherService).delete(5);
        mockMvc.perform(post("/teacher/delete").param("id", "5"))
               .andExpect(status().isOk())
               .andExpect(view().name("/teacher/delete"));
    }
    
    interface TestData {

        Integer teacherId2 = 2;
        Integer teacherId3 = 3;
        Integer courseId = 2;
        Integer courseId2 = 3;
        String firstName = "Zoe";
        String lastName = "Barnes";

        Teacher teacher = Teacher.builder().firstName(firstName).lastName(lastName).build();
        Teacher teacher2 = Teacher.builder().id(teacherId2).firstName("Peter").lastName("Russo").build();
        Teacher teacher3 = Teacher.builder().id(teacherId3).firstName("Lucas").lastName("Goodwin").build();
        List<Teacher> teachers = asList(teacher, teacher2, teacher3);
        List<Teacher> teachers2 = asList(teacher3);
        
        TeacherDto teacherDto = TeacherDto.builder().firstName(firstName).lastName(lastName).build();
        TeacherDto teacherDto2 = TeacherDto.builder().id(teacherId2).firstName("Peter").lastName("Russo").build();
    }

}
