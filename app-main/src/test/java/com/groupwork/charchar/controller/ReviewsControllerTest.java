package com.groupwork.charchar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupwork.charchar.entity.ReviewsEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        int[] ids = {1,2,3};
        objectMapper = new ObjectMapper();
        testReviews = new ReviewsEntity();
        testReviews.setReviewId(002);
        testReviews.setAttractionId(001);
        testReviews.setUserId(001);
        testReviews.setRating(5);
        testReviews.setReviewText("Quayside is fantastic! We have great time here!");

    }

    /**
     *获取某个景点的所有评论
     @throws Exception 測試過程中若有例外拋出，則代表測試失敗。
     */
    @Test
    @Order(1)
    public void shouldReturnOKWhenGetReviewsList() throws Exception {
        this.mockMvc.perform(get("/charchar/reviews/list/attr/{attractionId}",testReviews.getAttractionId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.code").value(0))
                //.andExpect(jsonPath("$.msg").value("success"));
    }

    /**
     *分頁-获取某个景点的所有评论
     *目前先假定 page/review 1:10
     @throws Exception 測試過程中若有例外拋出，則代表測試失敗。
     */
    @Test
    @Order(1)
    public void pageShouldReturnOKWhenGetReviewsList() throws Exception {
        this.mockMvc.perform(get("/charchar/reviews/list/attr/{attractionId}/{page}/{size}",testReviews.getAttractionId(),1,10))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.code").value(0))
                //.andExpect(jsonPath("$.msg").value("success"));
    }


    /**
     *获取(已刪除)不存在景点的评论，應返回錯誤
     @throws Exception 測試過程中若有例外拋出，則代表測試失敗。
     ＊＊＊仍在確認更改中
     */
    @Test
    @Order(2)
    public void shouldReturnEmptyWhenGetNotExistReviews() throws Exception {
        this.mockMvc.perform(get("/charchar/reviews/info/{reviewsId}", testReviews.getReviewId()))
                .andExpect(status().is4xxClientError());
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$.code").value(0))
                //.andExpect(jsonPath("$.msg").value("success"))
                //.andExpect(jsonPath("$.reviews").isEmpty());
    }

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

    /**
     *存取評論
     @throws Exception 測試過程中若有例外拋出，則代表測試失敗。
     TODO:Return ID?
     */
    @Test
    @Order(3)
    public void shouldReturnOKWhenSaveReview() throws Exception {
        this.mockMvc.perform(post("/charchar/reviews/save")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testReviews))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
                //.andExpect(jsonPath("$.code").value(0))
                //.andExpect(jsonPath("$.msg").value("success"));
    }


//    /*
//    確認是否能使用review id 拿取對應資料：
//     */
//    @Test
//    @Order(5)
//    public void shouldReturnTestReviewWhenGetReviewInfo() throws Exception {
//        MvcResult result = this.mockMvc.perform(get("/product/reviews/info/{reviewsId}", testReviews.getReviewId())).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
//        ReviewsEntity dbReview =
//                objectMapper.readValue(response.getContentAsString(), R.class).getData("reviews", new TypeReference<ReviewsEntity>() {
//                });
//        //因指令時間不同 故需拉出要比對的4個項目
//        assertEquals(testReviews.getAttractionId(), dbReview.getAttractionId());
//        assertEquals(testReviews.getUserId(), dbReview.getUserId());
//        assertEquals(testReviews.getRating(), dbReview.getRating());
//        assertEquals(testReviews.getReviewText(), dbReview.getReviewText());
//    }

    /**
     *修改評論
     @throws Exception 測試過程中若有例外拋出，則代表測試失敗。
     TODO:Id needed?
     */
    @Test
    @Order(6)
    public void shouldReturnOKWhenUpdateReview() throws Exception {
        String newReviewText = "newReviewTest";
        testReviews.setReviewText(newReviewText);
        // Check update function
        this.mockMvc.perform(put("/charchar/reviews/update")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testReviews))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // Confirming success of update
        //this.mockMvc.perform(get("/product/reviews/info/{userId}", testReviews.getUserId()))
          //      .andExpect(status().isOk())
            //    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              //  .andExpect(jsonPath("$.reviews.reviewText").value(newReviewText));
    }

    /**
     *获取某个用戶的所有评论
     @throws Exception 測試過程中若有例外拋出，則代表測試失敗。
     */

    @Test
    @Order(6)
    public void shouldReturnOKWhenGetReviewsListFromAUser() throws Exception {
        this.mockMvc.perform(get("/charchar/reviews/list/attr/{userId}",testReviews.getUserId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //.andExpect(jsonPath("$.code").value(0))
        //.andExpect(jsonPath("$.msg").value("success"));
    }

    /**
     *分頁 某个用戶的所有评论
     * 目前先假定 page/review 1:5
     @throws Exception 測試過程中若有例外拋出，則代表測試失敗。
     */

    @Test
    @Order(6)
    public void pageShouldReturnOKWhenGetReviewsListFromAUser() throws Exception {
        this.mockMvc.perform(get("/charchar/reviews/list/user/{userId}/{page}/{size}",testReviews.getUserId(),1,5))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //.andExpect(jsonPath("$.code").value(0))
        //.andExpect(jsonPath("$.msg").value("success"));
    }

    /**
     *刪除用戶评论
     @throws Exception 測試過程中若有例外拋出，則代表測試失敗。
     TODO:跑錯目前仍在確認
     */
    @Test
    @Order(7)
    //public void shouldReturnOKWhenDeleteReview() throws Exception {
        // Check update function
      //  String jsonStr = "[" + testReviews.getUserId() + "]";
        //this.mockMvc.perform(post("/charcahr/reviews/delete")
          //              .contentType("application/json")
            //            .content("[1,2,3]")
              //  )
                //.andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON));
                //.andExpect(jsonPath("$.code").value(0))
                //.andExpect(jsonPath("$.msg").value("success"));
                // Confirming success of delete
                //this.mockMvc.perform(get("/product/reviews/info/{userId}", testReviews.getUserId()))
                //      .andExpect(status().isOk())
                //    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$.code").value(0))
                //.andExpect(jsonPath("$.msg").value("success"))
               // .andExpect(jsonPath("$.reviews").isEmpty());
    //}
    public void shouldReturnOKWhenDeleteReview() throws Exception {
        String jsonStr = "[1, 2, 3]";
        this.mockMvc.perform(delete("/charcahr/reviews/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStr))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


}
