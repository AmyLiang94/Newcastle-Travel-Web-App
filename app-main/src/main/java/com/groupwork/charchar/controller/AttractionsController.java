package com.groupwork.charchar.controller;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.groupwork.charchar.vo.UpdateAttractionRatingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationListenerMethodAdapter;
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
    public String getById(@PathVariable Integer attractionId) {
        AttractionsEntity attractionsEntity = attractionsService.getById(attractionId);
        System.out.println("getById bookList" + attractionsEntity);

        return "getById";
    }

    /**
     * 开门景点
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
     * 今天营业时间
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
     * 本周7天营业时间
     * @param attactionId
     * @return
     */

    @GetMapping("/openingHoursForTheWeek/{attractionId}")
    public List<List<LocalTime>> getOpeningHoursThisWeek ( @PathVariable Integer attactionId){
        List<List<LocalTime>> operationTimesThisWeek = new ArrayList<>();
        List<LocalTime> openingTimesThisWeek = new ArrayList<>();
        List<LocalTime> closingTimesThisWeek = new ArrayList<>();
        for (int i=0; i<7 ;i++){
            String[] tempStringList;
            DayOfWeek temDOW = DayOfWeek.of(i);
            String tempSlot=attractionsService.getOpeningHours(attactionId.toString(), temDOW);//Get the Operation Hours in string format.
            tempStringList = tempSlot.split("-");//Break the string into 2 pieces
            String tempSlotOpen=tempStringList[0];//Initialize the string respectively.
            String tempSlotClose=tempStringList[1];
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("HHmm");//Parse the string format back into time format
            LocalTime openingTime = LocalTime.parse(tempSlotOpen, parser);
            LocalTime closingTime = LocalTime.parse(tempSlotClose, parser);
            openingTimesThisWeek.add((openingTime));
            closingTimesThisWeek.add((closingTime));




        }
        operationTimesThisWeek.add(openingTimesThisWeek);
        operationTimesThisWeek.add(closingTimesThisWeek);

        return operationTimesThisWeek;

    }

    /**
     * 根据评分排序景点
     * @param attractionsEntityList
     * @return
     */


    @GetMapping("/sortAttractionByRating")
    public List<AttractionsEntity> sortattractionsByRating (List<AttractionsEntity> attractionsEntityList){

        for (int i = 1; i<attractionsEntityList.size(); i++){
            Double rating = attractionsEntityList.get(i).getAttrRating();
            int j;
            for (j = i; j>0; j--){

                if (attractionsEntityList.get(j-1).getAttrRating().compareTo(rating)<0){
                    break;
                }
                else {
                    attractionsEntityList.set(j,attractionsEntityList.get(j-1));
                }
            }

        }
        return attractionsEntityList;


    }


    /**
     * 根据种类过滤景点
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


    /**
     * 根据轮椅使用过滤景点
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
