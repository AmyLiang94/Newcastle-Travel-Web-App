package com.groupwork.charchar.controller;

/**
 * @Author zhy
 * @Date 2023 05 05 09 47
 **/

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupwork.charchar.vo.TopReviewsVO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @Order(1)
    public void shouldReturnMapWhenLike() throws Exception {
        String result  = this.mockMvc.perform(post("http://localhost:9001/charchar/like/giveLike/{userId}/{reviewId}",1,1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();;
        System.out.println(result);

    }


    @Test
    @Order(2)
    public void shouldReturnOKWhenUpdateReviewLikeCount() throws Exception {
        this.mockMvc.perform(post("http://localhost:9001/charchar/like/updateReviewLike/{attractionId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @Order(3)
    public void shouldReturnListWhenGetTopReviews() throws Exception {
        String result  =  this.mockMvc.perform(get("http://localhost:9001/charchar/like/getTopReviews/{attractionId}/{topN}",1,1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();;
        List<TopReviewsVO> topReviewsVOList = new ObjectMapper().readValue(result,
                new TypeReference<List<TopReviewsVO>>(){});
        System.out.println(topReviewsVOList);
    }


}
