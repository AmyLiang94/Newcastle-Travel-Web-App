package com.groupwork.charchar.controller;

import com.google.maps.errors.ApiException;
import com.groupwork.charchar.entity.AttractionsEntity;
import com.groupwork.charchar.service.AttractionsService;
import com.groupwork.charchar.vo.UpdateAttractionRatingVO;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author Jiahe "Tony" & Yilong Wang
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("charchar/attractions")
public class AttractionsController {
    @Autowired
    private AttractionsService attractionsService;

    /**
     * 保存一堆景点
     */
    @PostMapping("/save/AttractionList/{latitude}/{longitude}/{radius}")
    public void saveAttractionsList(@PathVariable ("latitude") double lat,
                                    @PathVariable ("longitude") double lng,
                                    @PathVariable ("radius") double rad) throws JSONException, IOException {
        List <AttractionsEntity> tempattractionList= attractionsService.saveNearByAttraction(lat, lng,rad);
        for (AttractionsEntity a :tempattractionList){
            boolean exists = attractionsService.checkPlaceIdExists(a.getPlaceId());
            if (!exists) {
                attractionsService.save(a);
            }
        }
    }
    /**
     * Return NearBy Attractions
     * Given User's Current Coordinate and Search Radius, Return A List Of AttractionEntities
     */
    @GetMapping("/near/location/{latitude}/{longitude}/{radius}")
    //need users's lat and lng coord as double, and radius as double in (M)
    public List<AttractionsEntity> getNearByLocation(@PathVariable("latitude") double latitude,
                                                     @PathVariable("longitude") double longitude,
                                                     @PathVariable("radius") double radius) throws IOException, JSONException {
        List<AttractionsEntity> res = attractionsService.getNearByLocation(latitude, longitude, radius);
        return res;
    }
    /**
     * Return Distance To The Attraction
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
     *Update Rating
     */
    @PostMapping("/update/rating/{attractionId}")
    public UpdateAttractionRatingVO updateAttractionRating(
            @PathVariable Integer attractionId) {
        UpdateAttractionRatingVO updateEntity = attractionsService.updateAttractionRating(attractionId);
        return updateEntity;
    }
    /**
     * 通过本地AttractionID 获取地点信息
     *
     */
    @GetMapping("/findAttractionByID/{attractionId}")
    public @ResponseBody AttractionsEntity getById(
            @PathVariable Integer attractionId) {
        AttractionsEntity attractionsEntity = attractionsService.getById(attractionId);
        return attractionsEntity;
    }
    /**
     * 过滤开门景点 二式改
     *
     */
    @GetMapping("/filterOpenAttractionsMK2")
    public @ResponseBody List<String> getAttractionByOpeningStatus(
            @RequestBody List<String> attractionsGoogleIDs)
            throws IOException, InterruptedException, ApiException, JSONException {

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        List <String > filteredAttractionsGoogleID = new ArrayList<>();

        for (String a : attractionsGoogleIDs){


            int openingStatus=attractionsService.getCurrentOpeningStatus(a);
            if (openingStatus != 0){
                filteredAttractionsGoogleID.add(a);
            }
        }
        return filteredAttractionsGoogleID;
    }

    /**
     * 根据评分排序景点

     */
    @GetMapping("/sortAttractionByRating/")
    public @ResponseBody List<String> sortAttractionsByRating (
            @RequestBody List<String> attractionsGoogleIDs) throws JSONException, IOException {
        for (int i = 1; i<attractionsGoogleIDs.size(); i++){
            String current = attractionsGoogleIDs.get(i);
            int j = i-1;
            while (j>=0 && getRatingFromGoogle(attractionsGoogleIDs.get(j))<getRatingFromGoogle(current)){
                attractionsGoogleIDs.set(j+1 , attractionsGoogleIDs.get(j));
                j--;
            }
            attractionsGoogleIDs.set(j+1 , current);

        }

        return attractionsGoogleIDs;

    }
    /**
     * 根据种类过滤景点

     */
    @GetMapping("/filterAttractionByCategory/{category}")
    public @ResponseBody List<AttractionsEntity> getAttractionByCategory(@RequestBody List<Integer> attractionIDs,
                                                                         @PathVariable("category") String category) {
        List<AttractionsEntity> filteredAttractions = attractionsService.filterAttractionByCategory(attractionIDs, category);
        return filteredAttractions;
    }
    /**
     * 根据轮椅使用过滤景点
     */
    @GetMapping("/filterAttractionByWheelChairAccessibility/{wc_allowed}")
    public @ResponseBody List<String> getAttractionByWheelChairAccessibility(
            @RequestBody List<String> attracGoogleId,
            @PathVariable("wc_allowed") Integer wc_allowed) throws JSONException, IOException {
        List<String> result = attractionsService.filterAttractionByWheelChairAccessibility(attracGoogleId, wc_allowed);
        return result;
    }
    /**
     * 根据距离排序
     *
     */
    @GetMapping("/sortAttractionByDistance/{departLat}/{departLng}")
    public @ResponseBody List<String> sortAttractionByDistance(@RequestBody List<String> attractionsGoogleIdList,
                                                            @PathVariable ("departLat") double departLat,
                                                            @PathVariable ("departLng")double departLng) throws JSONException, IOException {


        for (int i = 1; i<attractionsGoogleIdList.size(); i++){
            String current = attractionsGoogleIdList.get(i);
            int j = i-1;
            while (j>=0 && Double.parseDouble(attractionsService.getWalkTime(departLat,departLng,attractionsService.getLatCoordByGoogleID(attractionsGoogleIdList.get(j)),attractionsService.getLngCoordByGoogleID(attractionsGoogleIdList.get(j))))<
                    Double.parseDouble(attractionsService.getWalkTime(departLat,departLng,attractionsService.getLatCoordByGoogleID(current),attractionsService.getLngCoordByGoogleID(current)))
            )


            {
                attractionsGoogleIdList.set(j+1 , attractionsGoogleIdList.get(j));
                j--;
            }
            attractionsGoogleIdList.set(j+1 , current);

        }

        return attractionsGoogleIdList;
    }
    /**
     * 根据景点热门程度排序
     */
    @GetMapping("/sortAttractionByPopularity/")
    public @ResponseBody List<String> sortAttractionByPopular(@RequestBody List<String> attractionsGoogleIdList) throws JSONException, IOException {
        for (int i = 1; i<attractionsGoogleIdList.size(); i++){
            String current = attractionsGoogleIdList.get(i);
            int j = i-1;
            while (j>=0 && attractionsService.getTotalNumberOfRatingsByGoogleID(attractionsGoogleIdList.get(j))
                    <
                    attractionsService.getTotalNumberOfRatingsByGoogleID(current)){
                attractionsGoogleIdList.set(j+1 , attractionsGoogleIdList.get(j));
                j--;
            }
            attractionsGoogleIdList.set(j+1 , current);

        }

        return attractionsGoogleIdList;
    }
    /**
     * 根据景点免费分类
     *
     */
    @GetMapping("/filterAttractionByFreeEntry/")
    public @ResponseBody List<AttractionsEntity> getAttractionsByFreeEntry (
            @RequestBody List<AttractionsEntity> attractionsEntityList){
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
    public @ResponseBody List<AttractionsEntity> sortAttractionByTicketPrice(
            @RequestBody List<AttractionsEntity> attractionsEntityList){
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
     * 获取谷歌PlaceID By Name
     */
    @GetMapping("/getAttractionGooglePlaceIDByName/{attractionName}")
    public @ResponseBody String getAttractionGooglePlaceIDByName(
            @PathVariable ("attractionName") String attractionName)
            throws JSONException, IOException {
        String result = null;
        result = attractionsService.getGooglePlaceIDByName(attractionName);
        return result;
    }
    /**
     * 获取谷歌ID By Name ans Address
     *
     */
    @GetMapping("/getAttractionGooglePlaceIDByNameAndCoord/{attractionName}/{attractionAddress}")
    public @ResponseBody String getAttractionGooglePlaceIDByNameAndCoord(
            @PathVariable ("attractionName") String attractionName,
            @PathVariable ("attractionAddress") String attractionAddress)
            throws JSONException, IOException {
        return attractionsService.getGooglePlaceIDByCoordinateAndName(attractionName,attractionAddress);
    }

    /**
     *通过Google ID获取地点轮椅使用
     */
    @GetMapping("/getWC_AccessiilityFromGoogle/{attractionGoogleID}")
    public @ResponseBody int getWC_AccessiilityFromGoogle (@PathVariable("attractionGoogleID") String googleID)
            throws JSONException, IOException {

        return attractionsService.getWheelChair_AccessblityByGoogleID(googleID);
    }
    /**
     * 通过Google ID获取地点评分
     */
    @GetMapping("/getRatingFromGoogle/{attractionGoogleID}")
    public @ResponseBody double getRatingFromGoogle (@PathVariable("attractionGoogleID") String googleID)
            throws JSONException, IOException {

        return attractionsService.getRatingByGoogleID(googleID);
    }
    /**
     * 通过Google ID获取地点种类
     */
    @GetMapping("/getCategoryFromGoogle/{attractionGoogleID}")
    public @ResponseBody String getCategoryFromGoogle (@PathVariable("attractionGoogleID") String googleID)
            throws JSONException, IOException {
        return attractionsService.getCategoryByGoogleID(googleID);
    }

    /**
     * 通过Google ID获取地点地址
     */
    @GetMapping("/getAddressFromGoogle/{attractionGoogleID}")
    public @ResponseBody String getAddressFromGoogle (@PathVariable("attractionGoogleID") String googleID)
            throws JSONException, IOException {
        return attractionsService.getAddressByGoogleID(googleID);
    }
    /**
     * 通过Google ID 获得地点电话
     */
    @GetMapping("/getPhoneFromGoogle/{attractionGoogleID}")
    public @ResponseBody String getPhoneFromGoogle (@PathVariable("attractionGoogleID") String googleID)
            throws JSONException, IOException {
        return attractionsService.getPhoneNumberByGoogleID(googleID);
    }
    /**
     * 通过Google ID获取地点官网
     */
    @GetMapping("/getOfficalWebsiteFromGoogle/{attractionGoogleID}")
    public @ResponseBody String getOfficalWebsiteFromGoogle (@PathVariable("attractionGoogleID") String googleID)
            throws JSONException, IOException {
        return attractionsService.getOfficalWebsiteByGoogleID(googleID);
    }
    /**
     * 通过Google ID获取地点总评分数量
     */
    @GetMapping("/getNumberOfReviewsFromGoogle/{attractionGoogleID}")
    public @ResponseBody int getNumberOfReviewsFromGoogle (@PathVariable("attractionGoogleID") String googleID)
            throws JSONException, IOException {
        return attractionsService.getTotalNumberOfRatingsByGoogleID(googleID);
    }
    /**
     * 通过Google ID获取地点简介
     */
    @GetMapping("/getOverViewFromGoogle/{attractionGoogleID}")
    public @ResponseBody String getOverViewFromGoogle (@PathVariable("attractionGoogleID") String googleID)
            throws JSONException, IOException {
        return attractionsService.getOverViewByGoogleID(googleID);
    }
    /**
     * 本周7天营业时间
     *

     */
    @GetMapping("/openingHoursForTheWeek/{attractionGoogleID}")
    public @ResponseBody List<String> getOpeningHoursThisWeek (
            @PathVariable ("attractionGoogleID") String attractionGoogleID)
            throws JSONException, IOException {

        List<String> timeList;
        timeList = attractionsService.getOpeningHourMK2(attractionGoogleID);
        return timeList;
    }




    /**
     * Saving a attraction
     *
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
