package com.groupwork.charchar.controller;

/**
 * @Author zhy
 * @Date 2023 05 05 09 47
 **/

import com.alibaba.fastjson.TypeReference;
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
        String result  = this.mockMvc.perform(post("http://localhost:1234/charchar/like/giveLike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"userId\": 1,\n" +
                                "  \"reviewId\": 1,\n" +
                                "}")
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
        this.mockMvc.perform(post("http://localhost:1234/charchar/like/updateReviewLike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"attractionId\": 1,\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @Order(3)
    public void shouldReturnListWhenGetTopReviews() throws Exception {
        String result  =  this.mockMvc.perform(get("http://localhost:1234/charchar/like/getTopReviews/{attractionId}/{topN}",1,1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();;
        List<TopReviewsVO> topReviewsVOList = new ObjectMapper().readValue(result,
                new TypeReference<List<TopReviewsVO>>(){});
        System.out.println(topReviewsVOList);
    }


}
