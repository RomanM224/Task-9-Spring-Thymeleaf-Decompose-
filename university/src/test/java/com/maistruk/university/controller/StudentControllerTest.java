package com.maistruk.university.controller;

import static com.maistruk.university.controller.StudentControllerTest.TestData.student2;
import static com.maistruk.university.controller.StudentControllerTest.TestData.student5;
import static com.maistruk.university.controller.StudentControllerTest.TestData.student6;
import static com.maistruk.university.controller.StudentControllerTest.TestData.studentDto;
import static com.maistruk.university.controller.StudentControllerTest.TestData.studentDto2;
import static com.maistruk.university.controller.StudentControllerTest.TestData.students;
import static com.maistruk.university.controller.StudentControllerTest.TestData.students2;
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
import com.maistruk.university.exceptions.StudentException;
import com.maistruk.university.mapper.StudentMapper;
import com.maistruk.university.model.Student;
import com.maistruk.university.model.dto.StudentDto;
import com.maistruk.university.service.StudentService;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = TestConfig.class)
public class StudentControllerTest {

    @Mock
    private StudentService studentService;
    @Mock
    private View mockView;
    @Mock
    private StudentMapper studentMapper;
    @InjectMocks
    private StudentController studentController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(studentController).setSingleView(mockView).build();
    }

    @Test
    public void givenShowAllURI_whenGetAll_thenSuccess() throws Exception {
        when(studentService.getAll()).thenReturn(students);
        mockMvc.perform(get("/student/showAll"))
               .andExpect(model().attribute("students", students))
               .andExpect(status().isOk())
               .andExpect(view().name("/student/showAll"));
    }
    
    @Test
    public void givenGetByIdURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/student/getById"))
               .andExpect(status().isOk())
               .andExpect(view().name("/student/getById"));
    }
    
    @Test
    public void givenStudentId_whenGetById_thenSuccess() throws Exception {
        when(studentService.getById(6)).thenReturn(student6);
        mockMvc.perform(post("/student/getById").param("id", "6"))
               .andExpect(model().attribute("students", students2))
               .andExpect(status().isOk())
               .andExpect(view().name("/student/showAll"));
    }
    
    @Test
    public void givenCreateURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/student/create"))
               .andExpect(status().isOk())
               .andExpect(view().name("/student/create"));
    }
    
    @Test
    public void givenStudent_whenCreate_thenSuccess()  throws Exception{
        when(studentMapper.mapStudentDtoToStudent(studentDto)).thenReturn(student2);
        mockMvc.perform(post("/student/create").param("firstName", "Kate").param("lastName", "Watson"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(studentService).create(student2);
    }
    
    @Test
    public void givenWrongStudent_whenCreate_thenThrowException()  throws Exception{
        when(studentMapper.mapStudentDtoToStudent(studentDto)).thenReturn(student2);
        doThrow(StudentException.class).when(studentService).create(student2);
        mockMvc.perform(post("/student/create").param("firstName", "Kate").param("lastName", "Watson"))
               .andExpect(status().isOk())
               .andExpect(view().name("/student/create"));
    }
    
    @Test
    public void givenUpdateURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/student/update"))
               .andExpect(status().isOk())
               .andExpect(view().name("/student/update"));
    }
    
    @Test
    public void givenStudent_whenUpdate_thenSuccess()  throws Exception{
        when(studentMapper.mapStudentDtoToStudent(studentDto2)).thenReturn(student5);
        mockMvc.perform(post("/student/update").param("id", "5").param("firstName", "Sophie").param("lastName", "Middleditch"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(studentService).update(student5);
    }
    
    @Test
    public void givenWrongStudent_whenUpdate_thenThrowException()  throws Exception{
        when(studentMapper.mapStudentDtoToStudent(studentDto2)).thenReturn(student5);
        doThrow(StudentException.class).when(studentService).update(student5);
        mockMvc.perform(post("/student/update").param("id", "5").param("firstName", "Sophie").param("lastName", "Middleditch"))
               .andExpect(status().isOk())
               .andExpect(view().name("/student/update"));
    }
    
    @Test
    public void givenDeleteURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/student/delete"))
               .andExpect(status().isOk())
               .andExpect(view().name("/student/delete"));
    }
    
    @Test
    public void givenStudentId_whenDelete_thenSuccess()  throws Exception{
        mockMvc.perform(post("/student/delete").param("id", "3"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(studentService).delete(3);
    }
    
    @Test
    public void givenWrongStudentId_whenDelete_thenThrowException()  throws Exception{
        doThrow(StudentException.class).when(studentService).delete(11);
        mockMvc.perform(post("/student/delete").param("id", "11"))
               .andExpect(status().isOk())
               .andExpect(view().name("/student/delete"));
    }
    
    interface TestData {
        Student student = Student.builder().id(1).firstName("Brianne").lastName("Wetterlund").build();
        Student student2 = Student.builder().firstName("Kate").lastName("Watson").build();
        Student student3 = Student.builder().id(3).firstName("Josh").lastName("Lawrence").build();
        Student student4 = Student.builder().id(4).firstName("Kate").lastName("Ross").build();
        Student student5 = Student.builder().id(5).firstName("Sophie").lastName("Middleditch").build();
        Student student6 = Student.builder().id(6).firstName("Matt").lastName("Brener").build();
        Student student7 = Student.builder().id(7).firstName("Martin").lastName("Shipp").build();
        Student student8 = Student.builder().id(8).firstName("Alice").lastName("Nanjiani").build();
        Student student9 = Student.builder().id(9).firstName("Emily").lastName("Connelly").build();
        Student student10 = Student.builder().id(10).firstName("Leonardo").lastName("Miller").build();

        List<Student> students = asList(student, student2, student3, student4, student5, student6, student7, student8,
                student9, student10);
        List<Student> students2 = asList(student6);
        
        StudentDto studentDto = StudentDto.builder().firstName("Kate").lastName("Watson").build();
        StudentDto studentDto2 = StudentDto.builder().id(5).firstName("Sophie").lastName("Middleditch").build();

    }

}
