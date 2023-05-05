package com.groupwork.charchar.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.google.maps.errors.ApiException;
import com.groupwork.charchar.entity.AttractionsEntity;
import com.groupwork.charchar.vo.UpdateAttractionRatingVO;

import org.json.JSONException;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.List;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
public interface AttractionsService extends IService<AttractionsEntity> {

    List<AttractionsEntity> getNearByLocation(double latitude, double longitude, double radius) throws IOException, JSONException;

    String getWalkTime(double departLat, double departLng, double desLat, double desLng);
    List<AttractionsEntity> filterAttractionByCategory(List<Integer> attractions, String category);
    List<String> filterAttractionByWheelChairAccessibility(List <String> attractionGoogleId, Integer wheelchairAllow) throws JSONException, IOException;
    List<String> getOpeningHourMK2(String placeID) throws JSONException, IOException;
    int getCurrentOpeningStatus(String placeID) throws JSONException, IOException;
    String getGooglePlaceIDByName(String attractionName) throws IOException, JSONException;
    String getGooglePlaceIDByCoordinateAndName(String attractionName, String attractionAddress) throws IOException, JSONException;
    int getWheelChair_AccessblityByGoogleID(String attractionGoogleID)throws IOException, JSONException;
    String getCategoryByGoogleID(String attractionGoogleID)throws IOException, JSONException;
    double getRatingByGoogleID(String attractionGoogleID) throws IOException, JSONException;
    String getPhoneNumberByGoogleID(String attractionGoogleI) throws IOException, JSONException;
    String getOfficalWebsiteByGoogleID(String attractionGoogleI)throws IOException, JSONException;
    int getTotalNumberOfRatingsByGoogleID(String attractionGoogleI)throws IOException, JSONException;
    String getAddressByGoogleID(String attractionGoogleI)throws IOException, JSONException;
    String getOverViewByGoogleID(String attractionGoogleI)throws IOException, JSONException;
    String getNameByGoogleID(String attractionGoogleI)throws IOException, JSONException;
    double getLatCoordByGoogleID(String attractionGoogleID)throws IOException, JSONException;
    double getLngCoordByGoogleID(String attractionGoogleID)throws IOException, JSONException;





    UpdateAttractionRatingVO updateAttractionRating(Integer attractionId);
}

