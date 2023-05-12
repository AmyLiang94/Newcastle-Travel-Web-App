package com.groupwork.charchar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupwork.charchar.entity.UsersEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private static ObjectMapper objectMapper; //轉換Json
    private static UsersEntity testUser;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        testUser = new UsersEntity();
        testUser.setUserId(999999);
        testUser.setUsername("Tester");
        testUser.setPassword("TesterPwd");
        testUser.setEmail("Tester@test.com");
    }


    /**
     * User login.
     * @throws Exception If an exception is thrown during the testing process, it indicates a test failure.
     */
    @Test
    @Order(1)
    public void shouldReturnOKWhenUserLogin() throws Exception {
        this.mockMvc.perform(post("/charchar/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    /**
     * Update user information.
     * @throws Exception If an exception is thrown during the testing process, it indicates a test failure.
     */
    @Test
    @Order(2)
    public void shouldReturnOKWhenUpdateUser() throws Exception {
        String newPwd = "newInformation";
        testUser.setPassword(newPwd);
        this.mockMvc.perform(post("/charchar/users/updateOneUserInfomation")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testUser))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Delete user comments.
     * @throws Exception If an exception is thrown during the testing process, it indicates a test failure.
     */
    @Test
    @Order(3)
    public void shouldReturnOKWhenDeleteUser() throws Exception {
        String jsonStr = "[" + testUser.getUserId() + "]";
        this.mockMvc.perform(delete("/charchar/users/deleteUser")
                        .contentType("application/json")
                        .content(jsonStr)
                )
                .andExpect(status().is4xxClientError());


    }
}
