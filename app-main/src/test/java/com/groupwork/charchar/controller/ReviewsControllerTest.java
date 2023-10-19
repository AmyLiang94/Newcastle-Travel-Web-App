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
    private static ObjectMapper objectMapper;
    private static ReviewsEntity testReviews;

    private ReviewsController reviewsController;


    @BeforeAll
    static void setUp(){
        objectMapper = new ObjectMapper();
        testReviews = new ReviewsEntity();
        testReviews.setReviewId(002);
        testReviews.setAttractionId(001);
        testReviews.setUserId(001);
        testReviews.setRating(5);
        testReviews.setReviewText("Quayside is fantastic! We have great time here!");

    }


    /**
     * Access comments.
     * @throws Exception If an exception is thrown during the testing process, it indicates a test failure.
     */
    @Test
    @Order(1)
    public void shouldReturnOKWhenSaveReview() throws Exception {
        this.mockMvc.perform(post("/charchar/reviews/save")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testReviews))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Get all comments from a specific user.
     * @throws Exception If an exception is thrown during the testing process, it indicates a test failure.
     */
    @Test
    @Order(2)
    public void shouldReturnOKWhenGetReviewsListFromAUser() throws Exception {
        this.mockMvc.perform(get("/charchar/reviews/list/user/{userId}",testReviews.getUserId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Delete user comments.
     * @throws Exception If an exception is thrown during the testing process, it indicates a test failure.
     */
    @Test
    @Order(3)

    public void shouldReturnOKWhenDeleteReview() throws Exception {
        this.mockMvc.perform(delete("/charchar/reviews/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1,2,3]"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    /**
     * Get all comments for a specific tourist attraction.
     * @throws Exception If an exception is thrown during the testing process, it indicates a test failure.
     */
    @Test
    @Order(4)
    public void shouldReturnOKWhenGetReviewsList() throws Exception {
        this.mockMvc.perform(get("/charchar/reviews/list/attr/{attractionId}/{userId}",testReviews.getAttractionId(),testReviews.getUserId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
