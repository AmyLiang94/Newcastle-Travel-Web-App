package com.groupwork.charchar.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.groupwork.charchar.vo.UpdateAttractionRatingVO;
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
     *
     * @param departLat latitude of departure
     * @param departLng longitude of departure
     * @param desLat    latitude of destination
     * @param desLng    longitude of destination
     */
    @GetMapping("/walktime/{departLat}/{departLng}/{desLat}/{desLng}")
    public String getWalkTime(@PathVariable("departLat") double departLat,
                              @PathVariable("departLng") double departLng,
                              @PathVariable("desLat") double desLat,
                              @PathVariable("desLng") double desLng) {
        String walkingTime = attractionsService.getWalkTime(departLat, departLng, desLat, desLng);

        return walkingTime;
    }

    /**
     *
     */
    @PostMapping("/update/rating/{attractionId}")
    public UpdateAttractionRatingVO updateAttractionRating(@PathVariable Integer attractionId) {
        UpdateAttractionRatingVO updateEntity = attractionsService.updateAttractionRating(attractionId);
        return updateEntity;
    }

    /**
     * acquire information about an attraction base on AttractionID
     *
     * @param attractionId
     * @return
     */


    @GetMapping("/findAttractionByID/{attractionId}")
    public AttractionsEntity getById(@PathVariable Integer attractionId){
        AttractionsEntity attractionsEntity = attractionsService.getById(attractionId);
        System.out.println("getById bookList" + attractionsEntity);
        return attractionsEntity;
    }

    /**
     * Filter the attraction based on their category
     *
     * @param attrac
     * @param category
     * @return
     */
    @GetMapping("/filterAttractionByCategory/{category}")
    public List<AttractionsEntity> getAttractionByCategory(@PathVariable List<AttractionsEntity> attrac, String category) {
        List<AttractionsEntity> filteredAttractions = attractionsService.filterAttractionByCategory(attrac, category);
        System.out.println("getAttractionByCategory" + filteredAttractions);
        return filteredAttractions;
    }

    @GetMapping("/filterattractionsByStillOpening")
    public List<AttractionsEntity> getAttractionThatStillOpen(@PathVariable List<AttractionsEntity> attrac) {
        return null;
    }

    /**
     * Selecting Attractions base on whether they allow wheelchair
     *
     * @param attrac
     * @param wc_allowed
     * @return
     */

    @GetMapping("/filterAttractionByWheelChairAccessibility/{wheelChairAccessibility}")
    public List<AttractionsEntity> getAttractionByWheelChairAccessibility(@PathVariable List<AttractionsEntity> attrac, Integer wc_allowed) {
        List<AttractionsEntity> result = attractionsService.filterAttractionByWheelChairAccessibility(attrac, wc_allowed);
        return result;
    }


    /**
     * Saving a attraction
     *
     * @param attractions
     * @return
     */
    @PostMapping("/save")
    public boolean saveAttractions(@RequestBody AttractionsEntity attractions) {
        attractionsService.save(attractions);
        return true;
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public boolean updateAttractions(@RequestBody AttractionsEntity attractions) {
        attractionsService.updateById(attractions);
        return true;
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public boolean deleteAttractions(@RequestBody Integer[] attractionsID) {
        attractionsService.removeByIds(Arrays.asList(attractionsID));
        return true;
    }


}
