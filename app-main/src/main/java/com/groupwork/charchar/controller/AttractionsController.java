package com.groupwork.charchar.controller;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
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

    /**
     * Acquire information about an attraction base on AttractionID
     * @param attractionId
     * @return String
     */
    @GetMapping("/findAttractionByID/{attractionId}")
    public AttractionsEntity getById(@PathVariable Integer attractionId){
        AttractionsEntity attractionsEntity = attractionsService.getById(attractionId);

        System.out.println("getById bookList"+attractionsEntity);

        return attractionsEntity;
    }

    /**
     * Returning the opening hour of the attraction of the day
     * @param attractionId
     * @return
     */
    @GetMapping("/getOperationHoursToday/{attractionId}")
    public String getOperationHoursToday(@PathVariable Integer attractionId){
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        String tempAttractionId = attractionId.toString();
        String operationHoursToday;
        operationHoursToday = attractionsService.getOpeningHours(tempAttractionId,dayOfWeek);
        return operationHoursToday;
    }

    /**
     * Filter out the opening attractions at the time of search
     * @param attractionsEntities
     * @return
     */
    @GetMapping("/filterOpenAttractions")
    public List<AttractionsEntity> getAttractionByOpeningStatus(@PathVariable List<AttractionsEntity> attractionsEntities){
        List<AttractionsEntity>  filteredAttractions = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        for (AttractionsEntity a : attractionsEntities){
            Integer tempId=a.getAttractionId();//Acquire ID
            String[] tempStringList;//Initialize a list of string to hold Opening time and Closing Time
            String tempSlot=attractionsService.getOpeningHours(tempId.toString(), dayOfWeek);//Get the Operation Hours in string format.
            tempStringList = tempSlot.split("-");//Break the string into 2 pieces
            String tempSlotOpen=tempStringList[0];//Initialize the string respectively.
            String tempSlotClose=tempStringList[1];
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("HHmm");//Parse the string format back into time format
            LocalTime openingTime = LocalTime.parse(tempSlotOpen, parser);
            LocalTime closingTime = LocalTime.parse(tempSlotClose, parser);
            if (now.isAfter(openingTime)&& now.isBefore(closingTime)){
                filteredAttractions.add(a);
            }
        }
        return filteredAttractions;
    }

    /**
     * Filter the attraction based on their category
     * @param attrac
     * @param category
     * @return
     */
    @GetMapping("/filterAttractionByCategory/{category}")
    public List<AttractionsEntity> getAttractionByCategory(@PathVariable List<AttractionsEntity> attrac, String category){
        List<AttractionsEntity> filteredAttractions = attractionsService.filterAttractionByCategory(attrac,category);
        System.out.println("getAttractionByCategory" + filteredAttractions);
        return filteredAttractions;
    }


    /**
     * Selecting Attractions base on whether they allow wheelchair
     * @param attrac
     * @param wc_allowed
     * @return
     */

    @GetMapping("/filterAttractionByWheelChairAccessibility/{wheelChairAccessibility}")
    public List<AttractionsEntity> getAttractionByWheelChairAccessibility(@PathVariable List<AttractionsEntity> attrac,Integer wc_allowed){
        List<AttractionsEntity> result = attractionsService.filterAttractionByWheelChairAccessibility(attrac,wc_allowed);
        return result;
    }


    /**
     * Saving a attraction
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
