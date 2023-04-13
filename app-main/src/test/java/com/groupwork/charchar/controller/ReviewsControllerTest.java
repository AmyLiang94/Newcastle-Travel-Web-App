package com.groupwork.charchar.controller;

import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.charchar.utils.R;
import com.groupwork.charchar.entity.ReviewsEntity;
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

public class ReviewsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static ObjectMapper objectMapper; //轉換Json
    private static ReviewsEntity testReviews;

    @BeforeAll
    static void setUp(){
        objectMapper = new ObjectMapper();
        testReviews = new ReviewsEntity();
        testReviews.setReviewId(001);
        testReviews.setAttractionId(001);
        testReviews.setUserId(001);
        testReviews.setRating(5);
        testReviews.setReviewText("Quayside is fantastic! We have great time here!");

    }

    /*
    Make sure it can return List
    確認可以返回List
     */
    @Test
    @Order(1)
    public void shouldReturnOKWhenGetReviewsList() throws Exception {
        this.mockMvc.perform(get("/product/reviews/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("success"));
    }

    /*
     * 需討論：status返回ok or 4XX
     * Make sure Not Exist User result
     */
    @Test
    @Order(2)
    public void shouldReturnEmptyWhenGetNotExistReviews() throws Exception {
        this.mockMvc.perform(get("/product/reviews/info/{reviewsId}", testReviews.getReviewId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.reviews").isEmpty());
    }

//    需討論：status返回ok or 4XX
//    @Test
//    @Order(2)
//    public void shouldReturn4xxWhenGetNotExistReviews() throws Exception {
//        this.mockMvc.perform(get("/product/reviews/info/{reviewsId}", testReviews.getReviewId()))
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
    public void shouldReturnOKWhenSaveReview() throws Exception {
        this.mockMvc.perform(post("/product/reviews/save")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testReviews))
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
    public void shouldReturn4xxWhenSaveExistReview() throws Exception {
        this.mockMvc.perform(post("/product/reviews/save")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testReviews))
                )
                .andExpect(status().is4xxClientError());
    }

    /*
    確認是否能使用review id 拿取對應資料：
     */
    @Test
    @Order(5)
    public void shouldReturnTestReviewWhenGetReviewInfo() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/product/reviews/info/{reviewsId}", testReviews.getReviewId())).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        ReviewsEntity dbReview =
                objectMapper.readValue(response.getContentAsString(), R.class).getData("reviews", new TypeReference<ReviewsEntity>() {
                });
        //因指令時間不同 故需拉出要比對的4個項目
        assertEquals(testReviews.getAttractionId(), dbReview.getAttractionId());
        assertEquals(testReviews.getUserId(), dbReview.getUserId());
        assertEquals(testReviews.getRating(), dbReview.getRating());
        assertEquals(testReviews.getReviewText(), dbReview.getReviewText());
    }

    /*
    確認update是否成功
     */
    @Test
    @Order(6)
    public void shouldReturnOKWhenUpdateReview() throws Exception {
        String newReviewText = "newReviewTest";
        testReviews.setReviewText(newReviewText);
        // Check update function
        this.mockMvc.perform(post("/product/reviews/update")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testReviews))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("success"));
        // Confirming success of update
        this.mockMvc.perform(get("/product/reviews/info/{userId}", testReviews.getUserId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.reviews.reviewText").value(newReviewText));
    }

    /*
    確認delete user是否成功
     */
    @Test
    @Order(7)
    public void shouldReturnOKWhenDeleteReview() throws Exception {
        // Check update function
        String jsonStr = "[" + testReviews.getUserId() + "]";
        this.mockMvc.perform(post("/product/reviews/delete")
                        .contentType("application/json")
                        .content(jsonStr)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("success"));
        // Confirming success of delete
        this.mockMvc.perform(get("/product/reviews/info/{userId}", testReviews.getUserId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.reviews").isEmpty());
    }


}
