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
    * 获取所有用户信息的所有信息List
     * @throws Exception 測試過程中若有例外拋出，則代表測試失敗。
     */
    @Test
    @Order(1)
    public void shouldReturnOKWhenGetUserList() throws Exception {
        this.mockMvc.perform(get("/charchar/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
                //.andExpect(jsonPath("$.code").value(0))
                //.andExpect(jsonPath("$.msg").value("success"));
    }

    /*
    TODO: 需討論以下情境的必要性
    Make sure Not Exist User result
     */
//    @Test
//    @Order(2)
//    public void shouldReturnEmptyWhenGetNotExistUserEmail() throws Exception {
//        this.mockMvc.perform(get("/charchar/users/getUserInformation")
//                .contentType("application/json")
//                .content(objectMapper.writeValueAsString(testUser)))
//                .andExpect(status().is4xxClientError()) //status().isOk()
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    //}
    



    /*
    確認是否能使用user id 拿取對應資料：
    寫法1:
     */
//    @Test
//    @Order(5)
//    public void shouldReturnTestUserWhenGetUserInfo() throws Exception {
////       MvcResult result = this.mockMvc.perform(get("/product/users/info/{userId}", testUser.getUserId())).andReturn();
////        MockHttpServletResponse response = result.getResponse();
////        assertEquals(HttpStatus.OK.value(), response.getStatus());
////        UsersEntity dbUser =
////                objectMapper.readValue(response.getContentAsString(), R.class).getData("users", new TypeReference<UsersEntity>() {
////                });
////        //因指令時間不同 故需拉出要比對的4個項目
////        assertEquals(testUser.getUsername(), dbUser.getUsername());
////        assertEquals(testUser.getPassword(), dbUser.getPassword());
////        assertEquals(testUser.getEmail(), dbUser.getEmail());
////        assertEquals(testUser.getPhone(), dbUser.getPhone());
////    }

    /*
    寫法2: 找json的值
    May 1th 為了ReviewsController 而comment
     */
     //*/
//    @Test
//    @Order(5)
//    public void shouldReturnTestUserWhenGetUserInfo2() throws Exception {
//        this.mockMvc.perform(get("/product/users/info/{userId}", testUser.getUserId()))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(0))
//                .andExpect(jsonPath("$.msg").value("success"))
//                .andExpect(jsonPath("$.users.username").value(testUser.getUsername()))
//                .andExpect(jsonPath("$.users.password").value(testUser.getPassword()))
//                .andExpect(jsonPath("$.users.email").value(testUser.getEmail()));
//                //.andExpect(jsonPath("$.users.phone").value(testUser.getPhone()));
//    }




     /**
     *修改用戶資料
     @throws Exception 測試過程中若有例外拋出，則代表測試失敗。
     */
    @Test
    @Order(2)
    public void shouldReturnOKWhenUpdateUser() throws Exception {
        String newPwd = "newInformation";
        testUser.setPassword(newPwd);
        // Check update function
        this.mockMvc.perform(post("/charchar/users/updateOneUserInfomation")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testUser))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
           //     .andExpect(jsonPath("$.code").value(0))
           //     .andExpect(jsonPath("$.msg").value("success"));
        // Confirming success of update
        //this.mockMvc.perform(get("/product/users/info/{userId}", testUser.getUserId()))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(0))
//                .andExpect(jsonPath("$.msg").value("success"))
//                .andExpect(jsonPath("$.users.password").value(newPwd));
    }

    /**
     *刪除用戶评论
     @throws Exception 測試過程中若有例外拋出，則代表測試失敗。
     TODO:跑錯目前仍在確認
     */
    @Test
    @Order(7)
    public void shouldReturnOKWhenDeleteUser() throws Exception {
        // Check update function
        String jsonStr = "[" + testUser.getUserId() + "]";
        this.mockMvc.perform(delete("/charchar/users/deleteUser")
                        .contentType("application/json")
                        .content(jsonStr)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//                .andExpect(jsonPath("$.code").value(0))
//                .andExpect(jsonPath("$.msg").value("success"));
//          Confirming success of delete
//          this.mockMvc.perform(get("/product/users/info/{userId}", testUser.getUserId()))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(0))
//                .andExpect(jsonPath("$.msg").value("success"))
//                .andExpect(jsonPath("$.users").isEmpty());

    }
}
