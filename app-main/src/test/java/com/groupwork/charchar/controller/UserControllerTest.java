package com.groupwork.charchar.controller;

import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.charchar.utils.R;
import com.groupwork.charchar.entity.UsersEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        testUser.setPhone("12345678");
    }


    /*
    Make sure it can return List
    確認可以返回List
     */
    @Test
    @Order(1)
    public void shouldReturnOKWhenGetUserList() throws Exception {
        this.mockMvc.perform(get("/product/users/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("success"));
    }

    /*
    需討論：status返回ok or 4XX
    Make sure Not Exist User result
     */
    @Test
    @Order(2)
    public void shouldReturnEmptyWhenGetNotExistUser() throws Exception {
        this.mockMvc.perform(get("/product/users/info/{userId}", testUser.getUserId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.users").isEmpty());
    }

//    需討論：status返回ok or 4XX
//    @Test
//    @Order(2)
//    public void shouldReturn4xxWhenGetNotExistUser() throws Exception {
//        this.mockMvc.perform(get("/product/users/info/{userId}", testUser.getUserId()))
//                .andExpect(status().is4xxClientError())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(0))
//                .andExpect(jsonPath("$.msg").value("success"))
//                .andExpect(jsonPath("$.users").isEmpty());
//    }


    /*
    確認是否儲存成功是否返OK
    Make sure save if it's saved
     */
    @Test
    @Order(3)
    public void shouldReturnOKWhenSaveUser() throws Exception {
        this.mockMvc.perform(post("/product/users/save")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testUser))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("success"));
    }


    /*
    跑錯！需討論：
    Controller should catch duplication exception and return status code 4xx
    確認是否儲存成功是否返OK
     */
    @Test
    @Order(4)
    public void shouldReturn4xxWhenSaveExistUser() throws Exception {
        this.mockMvc.perform(post("/product/users/save")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testUser))
                )
                .andExpect(status().is4xxClientError());
    }

    /*
    確認是否能使用user id 拿取對應資料：
    寫法1:
     */
    @Test
    @Order(5)
    public void shouldReturnTestUserWhenGetUserInfo() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/product/users/info/{userId}", testUser.getUserId())).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        UsersEntity dbUser =
                objectMapper.readValue(response.getContentAsString(), R.class).getData("users", new TypeReference<UsersEntity>() {
                });
        //因指令時間不同 故需拉出要比對的4個項目
        assertEquals(testUser.getUsername(), dbUser.getUsername());
        assertEquals(testUser.getPassword(), dbUser.getPassword());
        assertEquals(testUser.getEmail(), dbUser.getEmail());
        assertEquals(testUser.getPhone(), dbUser.getPhone());
    }

    /*
    寫法2: 找json的值
     */
    @Test
    @Order(5)
    public void shouldReturnTestUserWhenGetUserInfo2() throws Exception {
        this.mockMvc.perform(get("/product/users/info/{userId}", testUser.getUserId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.users.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.users.password").value(testUser.getPassword()))
                .andExpect(jsonPath("$.users.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.users.phone").value(testUser.getPhone()));
    }


    /*
    確認update是否成功
     */
    @Test
    @Order(6)
    public void shouldReturnOKWhenUpdateUser() throws Exception {
        String newPwd = "newPasswordTest";
        testUser.setPassword(newPwd);
        // Check update function
        this.mockMvc.perform(post("/product/users/update")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testUser))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("success"));
        // Confirming success of update
        this.mockMvc.perform(get("/product/users/info/{userId}", testUser.getUserId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.users.password").value(newPwd));
    }

    /*
    確認delete user是否成功
     */
    @Test
    @Order(7)
    public void shouldReturnOKWhenDeleteUser() throws Exception {
        // Check update function
        String jsonStr = "[" + testUser.getUserId() + "]";
        this.mockMvc.perform(post("/product/users/delete")
                        .contentType("application/json")
                        .content(jsonStr)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("success"));
        // Confirming success of delete
        this.mockMvc.perform(get("/product/users/info/{userId}", testUser.getUserId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.users").isEmpty());
    }
}
