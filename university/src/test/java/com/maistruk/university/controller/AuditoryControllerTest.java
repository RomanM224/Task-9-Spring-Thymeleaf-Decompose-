package com.maistruk.university.controller;

import static com.maistruk.university.controller.AuditoryControllerTest.TestData.auditories;
import static com.maistruk.university.controller.AuditoryControllerTest.TestData.auditories2;
import static com.maistruk.university.controller.AuditoryControllerTest.TestData.auditory;
import static com.maistruk.university.controller.AuditoryControllerTest.TestData.auditory2;
import static com.maistruk.university.controller.AuditoryControllerTest.TestData.auditory3;
import static com.maistruk.university.controller.AuditoryControllerTest.TestData.auditoryId2;
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
import com.maistruk.university.exceptions.AuditoryException;
import com.maistruk.university.model.Auditory;
import com.maistruk.university.service.AuditoryService;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = TestConfig.class)
public class AuditoryControllerTest {

    @Mock
    private AuditoryService auditoryService;
    @Mock
    private View mockView;
    @InjectMocks
    private AuditoryController auditoryController;
    private MockMvc mockMvc;
    
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(auditoryController).setSingleView(mockView).build();
    }

    @Test
    public void givenShowAllURI_whenGetAll_thenSuccess() throws Exception {
        when(auditoryService.getAll()).thenReturn(auditories);
        mockMvc.perform(get("/auditory/showAll"))
               .andExpect(model().attribute("auditories", auditories))
               .andExpect(status().isOk())
               .andExpect(view().name("/auditory/showAll"));
        verify(auditoryService).getAll();
    }
    
    @Test
    public void givenGetByIdURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/auditory/getById"))
               .andExpect(status().isOk())
               .andExpect(view().name("/auditory/getById"));
    }
    
    @Test
    public void givenAuditoryId_whenGetById_thenSuccess() throws Exception {
        when(auditoryService.getById(2)).thenReturn(auditory2);
        mockMvc.perform(post("/auditory/getById").param("id", "2"))
               .andExpect(model().attribute("auditories", auditories2))
               .andExpect(status().isOk())
               .andExpect(view().name("/auditory/showAll"));
    }
    
    @Test
    public void givenCreateURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/auditory/create"))
               .andExpect(status().isOk())
               .andExpect(view().name("/auditory/create"));
    }
    
    @Test
    public void givenAuditory_whenCreate_thenSuccess()  throws Exception{
        mockMvc.perform(post("/auditory/create").param("number", "100"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(auditoryService).create(auditory);
    }
    
    @Test
    public void givenWrongAuditory_whenCreate_thenThrowException()  throws Exception{
        doThrow(AuditoryException.class).when(auditoryService).create(auditory);
        mockMvc.perform(post("/auditory/create").param("number", "100"))
               .andExpect(status().isOk())
               .andExpect(view().name("/auditory/create"));
    }
    
    @Test
    public void givenUpdateURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/auditory/update"))
               .andExpect(status().isOk())
               .andExpect(view().name("/auditory/update"));
    }
    
    @Test
    public void givenAuditory_whenUpdate_thenSuccess()  throws Exception{
        mockMvc.perform(post("/auditory/update").param("id", "2").param("number", "101"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(auditoryService).update(auditory2);
    }
    
    @Test
    public void givenWrongAuditory_whenUpdate_thenThrowException()  throws Exception{
        doThrow(AuditoryException.class).when(auditoryService).update(auditory3);
        mockMvc.perform(post("/auditory/update").param("id", "3").param("number", "102"))
               .andExpect(status().isOk())
               .andExpect(view().name("/auditory/update"));
    }
    
    @Test
    public void givenDeleteURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/auditory/delete"))
               .andExpect(status().isOk())
               .andExpect(view().name("/auditory/delete"));
    }
    
    @Test
    public void givenAuditoryId_whenDelete_thenSuccess()  throws Exception{
        mockMvc.perform(post("/auditory/delete").param("id", "2"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(auditoryService).delete(auditoryId2);
    }
    
    @Test
    public void givenWrongAuditoryId_whenDelete_thenThrowException()  throws Exception{
        doThrow(AuditoryException.class).when(auditoryService).delete(4);
        mockMvc.perform(post("/auditory/delete").param("id", "4"))
               .andExpect(status().isOk())
               .andExpect(view().name("/auditory/delete"));
    }
    
    interface TestData {

        Integer auditoryId = 1;
        Integer auditoryId2 = 2;
        Integer auditoryId3 = 3;
        Integer auditoryNumber = 100;

        Auditory auditory = Auditory.builder().number(auditoryNumber).build();
        Auditory auditory2 = Auditory.builder().id(auditoryId2).number(101).build();
        Auditory auditory3 = Auditory.builder().id(auditoryId3).number(102).build();

        List<Auditory> auditories = asList(auditory, auditory2, auditory3);
        List<Auditory> auditories2 = asList(auditory2);
    }
}
