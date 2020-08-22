package com.maistruk.university.controller;

import static com.maistruk.university.controller.GroupControllerTest.TestData.group;
import static com.maistruk.university.controller.GroupControllerTest.TestData.group2;
import static com.maistruk.university.controller.GroupControllerTest.TestData.group3;
import static com.maistruk.university.controller.GroupControllerTest.TestData.groupId3;
import static com.maistruk.university.controller.GroupControllerTest.TestData.groups;
import static com.maistruk.university.controller.GroupControllerTest.TestData.groups2;
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
import com.maistruk.university.exceptions.GroupException;
import com.maistruk.university.model.Group;
import com.maistruk.university.service.GroupService;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = TestConfig.class)
public class GroupControllerTest {

    @Mock
    private GroupService groupService;
    @Mock
    private View mockView;
    @InjectMocks
    private GroupController groupController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(groupController).setSingleView(mockView).build();
    }

    @Test
    public void givenShowAllURI_whenGetAll_thenSuccess() throws Exception {
        when(groupService.getAll()).thenReturn(groups);
        mockMvc.perform(get("/group/showAll"))
               .andExpect(model().attribute("groups", groups))
               .andExpect(status().isOk())
               .andExpect(view().name("/group/showAll"));
    }
    
    @Test
    public void givenGetByIdURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/group/getById"))
               .andExpect(status().isOk())
               .andExpect(view().name("/group/getById"));
    }
    
    @Test
    public void givenGroupId_whenGetById_thenSuccess() throws Exception {
        when(groupService.getById(3)).thenReturn(group3);
        mockMvc.perform(post("/group/getById").param("id", "3"))
               .andExpect(model().attribute("groups", groups2))
               .andExpect(status().isOk())
               .andExpect(view().name("/group/showAll"));
    }
    
    @Test
    public void givenCreateURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/group/create"))
               .andExpect(status().isOk())
               .andExpect(view().name("/group/create"));
    }
    
    @Test
    public void givenGroup_whenCreate_thenSuccess()  throws Exception{
        mockMvc.perform(post("/group/create").param("name", "AK-27"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(groupService).create(group);
    }
    
    @Test
    public void givenWrongGroup_whenCreate_thenThrowException()  throws Exception{
        doThrow(GroupException.class).when(groupService).create(group);
        mockMvc.perform(post("/group/create").param("name", "AK-27"))
               .andExpect(status().isOk())
               .andExpect(view().name("/group/create"));
    }
    
    @Test
    public void givenUpdateURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/group/update"))
               .andExpect(status().isOk())
               .andExpect(view().name("/group/update"));
    }
    
    @Test
    public void givenGroup_whenUpdate_thenSuccess()  throws Exception{
        mockMvc.perform(post("/group/update").param("id", "2").param("name", "FJ-52"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(groupService).update(group2);
    }
    
    @Test
    public void givenWrongGroup_whenUpdate_thenThrowException()  throws Exception{
        doThrow(GroupException.class).when(groupService).update(group3);
        mockMvc.perform(post("/group/update").param("id", "3").param("name", "AE-49"))
               .andExpect(status().isOk())
               .andExpect(view().name("/group/update"));
    }
    
    @Test
    public void givenDeleteURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/group/delete"))
               .andExpect(status().isOk())
               .andExpect(view().name("/group/delete"));
    }
    
    @Test
    public void givenGroupId_whenDelete_thenSuccess()  throws Exception{
        mockMvc.perform(post("/group/delete").param("id", "3"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(groupService).delete(groupId3);
    }
    
    @Test
    public void givenWrongGroupId_whenDelete_thenThrowException()  throws Exception{
        doThrow(GroupException.class).when(groupService).delete(4);
        mockMvc.perform(post("/group/delete").param("id", "4"))
               .andExpect(status().isOk())
               .andExpect(view().name("/group/delete"));
    }
    
    interface TestData {

        Integer groupId = 1;
        Integer groupId2 = 2;
        Integer groupId3 = 3;

        String groupName = "AK-27";
        String groupName2 = "FJ-52";
        String groupName3 = "AE-49";

        Group group = Group.builder().name(groupName).build();
        Group group2 = Group.builder().id(groupId2).name(groupName2).build();
        Group group3 = Group.builder().id(groupId3).name(groupName3).build();
        List<Group> groups = asList(group, group2, group3);
        List<Group> groups2 = asList(group3);
    }


}
