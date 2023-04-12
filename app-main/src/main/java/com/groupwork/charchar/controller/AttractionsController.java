package com.groupwork.charchar.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.groupwork.charchar.entity.AttractionsEntity;
import com.groupwork.charchar.service.AttractionsService;


/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@RestController
@RequestMapping("product/attractions")
public class AttractionsController {
    @Autowired
    private AttractionsService attractionsService;


    /**
     * 返回范围内的景点
     */
    @GetMapping("/near/location/{latitude}/{longitude}/{radius}")
    public List<AttractionsEntity> getNearByLocation(@PathVariable("latitude") double latitude,
                                                     @PathVariable("longitude") double longitude,
                                                     @PathVariable("radius") double radius) throws IOException {
        List<AttractionsEntity> res = attractionsService.getNearByLocation(latitude, longitude, radius);
        return res;
    }
    /**
     * 获取步行时间
     * @param departLat  latitude of departure
     * @param departLng  longitude of departure
     * @param desLat   latitude of destination
     * @param desLng   longitude of destination
     */
    @GetMapping("/walktime/{departLat}/{departLng}/{desLat}/{desLng}")
    public String getWalkTime(@PathVariable("departLat") double departLat,
                              @PathVariable("departLng") double departLng,
                              @PathVariable("desLat") double desLat,
                              @PathVariable("desLng") double desLng) {
        String walkingTime = attractionsService.getWalkTime(departLat, departLng, desLat, desLng);
        return walkingTime;
    }

    @GetMapping("/{attractionId}")
    public String getById(@PathVariable Integer attractionId){
        AttractionsEntity attractionsEntity = attractionsService.getById(attractionId);
        System.out.println("getById bookList"+attractionsEntity);
        return "getById";
    }





    @PostMapping("/save/attractions")
    public boolean saveAttractions(@RequestBody AttractionsEntity attractions) {
        attractionsService.save(attractions);
        return true;
    }

    /**
     * 修改
     */
    @PutMapping("/update/attractions")
    public boolean updateAttractions(@RequestBody AttractionsEntity attractions) {
        attractionsService.updateById(attractions);
        return true;
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/attraction")
    public boolean deleteAttractions(@RequestBody Integer[] attractionsID) {
        attractionsService.removeByIds(Arrays.asList(attractionsID));
        return true;
    }



}
