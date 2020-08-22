package com.maistruk.university.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import com.maistruk.university.service.DatabaseInitializer;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = TestConfig.class)
public class HomeControllerTest {

    @Mock
    private DatabaseInitializer databaseInitializer;
    @Mock
    private View mockView;
    @InjectMocks
    private HomeController homeController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(homeController).setSingleView(mockView).build();
    }

    @Test
    public void givenEmptyURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("home"));
    }

    @Test
    public void givenGenerateInfoURI_whenInit_thenSuccess() throws Exception {
        mockMvc.perform(get("/generateInfo"))
               .andExpect(status().isOk())
               .andExpect(view().name("home"));
        verify(databaseInitializer).init();
    }

}
