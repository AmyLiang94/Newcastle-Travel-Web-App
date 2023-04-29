package com.groupwork.charchar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupwork.charchar.entity.AttractionsEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * @author zhuhaoyu
 * @email 784301058@qq.com
 * @date 2023-04-26 19:24:21
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class AttractionsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static ObjectMapper objectMapper;
    private static AttractionsEntity attractionsEntity;

    @BeforeAll
    static void setUp(){
        objectMapper = new ObjectMapper();
        attractionsEntity = new AttractionsEntity();
        attractionsEntity.setAttractionId(001);
        attractionsEntity.setAttractionName("test");
        attractionsEntity.setDescription("test");
        attractionsEntity.setLatitude(new BigDecimal("24.988778"));
        attractionsEntity.setLongitude(new BigDecimal("109.232323"));
        attractionsEntity.setOpeningHours("10");
        attractionsEntity.setTicketPrice(new BigDecimal("100"));

    }

    /*
    Make sure it can return List
    确认可以返回List
     */
    @Test
    @Order(1)
    public void shouldReturnOKWhenGetNearByLocationList() throws Exception {
        String result  = this.mockMvc.perform(get("product/attractions/near/location/{latitude}/{longitude}/{radius}",attractionsEntity.getLatitude(),attractionsEntity.getLongitude(),"10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();;
        List<AttractionsEntity> attractionsEntityList = new ObjectMapper().readValue(result,
                new TypeReference<List<AttractionsEntity>>(){});
        System.out.println(attractionsEntityList);
    }

    /*
     * 确认可以返回WalkingTime结果
     * Make sure Return result
     */
    @Test
    @Order(2)
    public void shouldReturnWalkingTimeWhenGetWalkTime() throws Exception {
        String result  =  this.mockMvc.perform(get("product/attractions//walktime/{departLat}/{departLng}/{desLat}/{desLng}", 24.09998,109.0987,24.087655,108.09873))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();;
        System.out.println(result);
    }

    /*
         确认update是否成功
       */
    @Test
    @Order(3)
    public void shouldReturnOKWhenUpdateAttractions() throws Exception {
        String result  = this.mockMvc.perform(post("/product/attractions/update/rating/{attractionId}",attractionsEntity.getAttractionId())
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();;
        AttractionsEntity attractionsEntity = new ObjectMapper().readValue(result,AttractionsEntity.class);
        System.out.println(attractionsEntity);

    }

    /*
       确认查询是否成功
    */
    @Test
    @Order(4)
    public void shouldReturnWhenFindAttractionByID() throws Exception {
        String result  =this.mockMvc.perform(get("/product/attractions/findAttractionByID/{attractionId}", attractionsEntity.getAttractionId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();;
        System.out.println(result);
    }

    /*
      确认查询开门景点是否返回结果
   */
    @Test
    @Order(5)
    public void shouldReturnWhenGetAttractionByOpeningStatus() throws Exception {
        List<AttractionsEntity> attractionsEntities  = new ArrayList<>();
        attractionsEntities.add(attractionsEntity);
        String result  = this.mockMvc.perform(get("/product/attractions/filterOpenAttractions", attractionsEntities))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();;
        System.out.println(result);
    }


    /*
      确认今天营业时间是否可以查询到
     */
    @Test
    @Order(6)
    public void shouldReturnTimeWhenGetOperationHoursToday() throws Exception {
        String result =  this.mockMvc.perform(get("/product/attractions/getOperationHoursToday/{attractionId}", attractionsEntity.getAttractionId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        List<AttractionsEntity> attractionsEntityList = new ObjectMapper().readValue(result,
                new TypeReference<List<AttractionsEntity>>(){});
        System.out.println(attractionsEntityList);
    }

    /*
     确认本周7天营业时间是否可以查询到
    */
    @Test
    @Order(7)
    public void shouldReturnTimeWhenGetOpeningHoursThisWeek() throws Exception {
        String result =  this.mockMvc.perform(get("/product/attractions/openingHoursForTheWeek/{attractionId}", attractionsEntity.getAttractionId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    /*
    根据评分排序景点
    */
    @Test
    @Order(8)
    public void shouldReturnWhenSortattractionsByRating() throws Exception {
        List<AttractionsEntity> attractionsEntities  = new ArrayList<>();
        attractionsEntities.add(attractionsEntity);
        String result = this.mockMvc.perform(get("/product/attractions/sortAttractionByRating", attractionsEntities))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        List<AttractionsEntity> attractionsEntityList = new ObjectMapper().readValue(result,
                new TypeReference<List<AttractionsEntity>>(){});
        System.out.println(attractionsEntityList);
    }

    /*
     根据种类过滤景点
   */
    @Test
    @Order(9)
    public void shouldReturnWhenGetAttractionByCategory() throws Exception {
        List<AttractionsEntity> attractionsEntities  = new ArrayList<>();
        attractionsEntities.add(attractionsEntity);
        String result = this.mockMvc.perform(get("/product/attractions/filterAttractionByCategory/{category}", attractionsEntities,attractionsEntity.getCategory()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        List<AttractionsEntity> attractionsEntityList = new ObjectMapper().readValue(result,
                new TypeReference<List<AttractionsEntity>>(){});
        System.out.println(attractionsEntityList);
    }

    /*
    根据轮椅使用过滤景点
    */
    @Test
    @Order(10)
    public void shouldReturnWheGetAttractionByWheelChairAccessibility() throws Exception {
        List<AttractionsEntity> attractionsEntities  = new ArrayList<>();
        attractionsEntities.add(attractionsEntity);
        String result = this.mockMvc.perform(get("/product/attractions/filterAttractionByWheelChairAccessibility/{wheelChairAccessibility}", attractionsEntities,111))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        List<AttractionsEntity> attractionsEntityList = new ObjectMapper().readValue(result,
                new TypeReference<List<AttractionsEntity>>(){});
        System.out.println(attractionsEntityList);
    }
    /*

   Controller should catch duplication exception and return status code 4xx
   确认是否儲存成功是否返OK
    */
    @Test
    @Order(11)
    public void shouldReturn4xxWhenSaveAttractions() throws Exception {
        this.mockMvc.perform(post("/product/attractions/save")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(attractionsEntity))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(true));;
    }

    /*
        确认update是否成功
    */
    @Test
    @Order(12)
    public void shouldReturnWhenUpdateAttractions() throws Exception {
        String newAttractText = "newtest";
        attractionsEntity.setAttractionName(newAttractText);
        // Check update function
        this.mockMvc.perform(post("/product/attractions/update")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(attractionsEntity))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true));

    }


    /*
        确认delete Attractions是否成功
     */
    @Test
    @Order(13)
    public void shouldReturnOKWhenDeleteAttractions() throws Exception {
        // Check update function
        String jsonStr = "[" + attractionsEntity.getAttractionId() + "]";
        this.mockMvc.perform(post("/product/attractions/delete")
                        .contentType("application/json")
                        .content(jsonStr)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true));
    }


}