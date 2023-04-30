package com.groupwork.charchar.controller;

import com.groupwork.charchar.entity.AttractionsEntity;
import com.groupwork.charchar.service.AttractionsService;
import com.groupwork.charchar.vo.UpdateAttractionRatingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@RestController
@RequestMapping("charchar/attractions")
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
    public Map<String, String> getWalkTime(@PathVariable("departLat") double departLat,
                                           @PathVariable("departLng") double departLng,
                                           @PathVariable("desLat") double desLat,
                                           @PathVariable("desLng") double desLng) {
        String walkingTime = attractionsService.getWalkTime(departLat, departLng, desLat, desLng);
        Map<String, String> response = new HashMap<>();
        response.put("walkingTime", walkingTime);
        return response;
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
     * 开门景点
     * @param attractionsEntities
     * @return
     */
    @GetMapping("/filterOpenAttractions")
    public @ResponseBody List<AttractionsEntity> getAttractionByOpeningStatus(@RequestBody List<AttractionsEntity> attractionsEntities){
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
    public @ResponseBody String getOperationHoursToday(@PathVariable("attractionId") Integer attractionId){
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
    public @ResponseBody List<List<LocalTime>> getOpeningHoursThisWeek ( @PathVariable("attractionId") Integer attactionId){
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
    public @ResponseBody List<AttractionsEntity> sortAttractionsByRating (@RequestBody List<AttractionsEntity> attractionsEntityList){
        for (int i = 1; i<attractionsEntityList.size(); i++){
            AttractionsEntity current = attractionsEntityList.get(i);
            int j = i-1;
            while (j>=0 && attractionsEntityList.get(j).getAttrRating()<current.getAttrRating()){
                attractionsEntityList.set(j+1 , attractionsEntityList.get(j));
                j--;
            }
            attractionsEntityList.set(j+1 , current);

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
    public @ResponseBody List<AttractionsEntity> getAttractionByCategory(@RequestBody List<AttractionsEntity> attrac,
                                                                         @PathVariable("category") String category) {
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

    @GetMapping("/filterAttractionByWheelChairAccessibility/{wc_allowed}")
    public @ResponseBody List<AttractionsEntity> getAttractionByWheelChairAccessibility(@RequestBody List<AttractionsEntity> attrac,
                                                                                        @PathVariable("wc_allowed") Integer wc_allowed) {
        List<AttractionsEntity> result = attractionsService.filterAttractionByWheelChairAccessibility(attrac, wc_allowed);
        return result;
    }
    @GetMapping("/sortAttractionByDistance/{departLat}/{departLng}")
    public @ResponseBody List<AttractionsEntity> sortAttractionByDistance(@RequestBody List<AttractionsEntity> attractionsEntityList,
                                                            @PathVariable ("departLat") double departLat,
                                                            @PathVariable ("departLng")double departLng){


        for (int i = 1; i<attractionsEntityList.size(); i++){
            AttractionsEntity current = attractionsEntityList.get(i);
            int j = i-1;
            while (j>=0
                    &&
                    Double.parseDouble(attractionsService.getWalkTime(departLat,
                    departLng,
                    attractionsEntityList.get(j).getLatitude().doubleValue(),
                    attractionsEntityList.get(j).getLongitude().doubleValue()))
                    <
                    Double.parseDouble(attractionsService.getWalkTime(departLat,
                    departLng,
                    current.getLatitude().doubleValue(),
                    current.getLongitude().doubleValue()))
                    ){
                attractionsEntityList.set(j+1 , attractionsEntityList.get(j));
                j--;
            }
            attractionsEntityList.set(j+1 , current);

        }

        return attractionsEntityList;
    }


    @GetMapping("/filterAttractionByFreeEntry/")
    public @ResponseBody List<AttractionsEntity> getAttractionsByFreeEntry (@RequestBody List<AttractionsEntity> attractionsEntityList){
        List<AttractionsEntity> filteredList = new ArrayList<>();
        for (AttractionsEntity a : attractionsEntityList){
            if (a.getTicketPrice().doubleValue()==0){
                filteredList.add(a);
            }
        }
        return filteredList;
    }

    /**
     * 根据景点价格排序
     *
     */
    @GetMapping("/sortAttractionByTicketPrice/")
    public @ResponseBody List<AttractionsEntity> sortAttractionByTicketPrice(@RequestBody List<AttractionsEntity> attractionsEntityList){
        for (int i = 1; i<attractionsEntityList.size(); i++){
            AttractionsEntity current = attractionsEntityList.get(i);
            int j = i-1;
            while (j>=0 && attractionsEntityList.get(j).getTicketPrice().doubleValue()<current.getTicketPrice().doubleValue()){
                attractionsEntityList.set(j+1 , attractionsEntityList.get(j));
                j--;
            }
            attractionsEntityList.set(j+1 , current);

        }
        return attractionsEntityList;

    }
    /**
     * Saving a attraction
     *
     * @param attractions
     * @return
     */
    @PostMapping("/save")
    public Map<String, Boolean> saveAttractions(@RequestBody AttractionsEntity attractions) {
        boolean success = attractionsService.save(attractions);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", success);
        return response;
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Map<String, Boolean> updateAttractions(@RequestBody AttractionsEntity attractions) {
        boolean success = attractionsService.updateById(attractions);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", success);
        return response;
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{attractionsID}")
    public Map<String, Boolean> deleteAttractions(@PathVariable Integer attractionsID) {
        boolean success = attractionsService.removeById(attractionsID);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", success);
        return response;
    }
}
