package com.groupwork.charchar.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.groupwork.charchar.entity.AttractionsEntity;

import java.io.IOException;
import java.util.List;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
public interface AttractionsService extends IService<AttractionsEntity> {

    List<AttractionsEntity> getNearByLocation(double latitude, double longitude, double radius) throws IOException;

    String getWalkTime(double departLat, double departLng, double desLat, double desLng);

    List<AttractionsEntity> filterAttractionByCategory(List<AttractionsEntity> attractions, String category);

    List<AttractionsEntity> filterAttractionByWheelChairAccessibility(List <AttractionsEntity> attractions, Integer wheelchairAllow);

    List<AttractionsEntity> filterAttractionByOpeningTime(List <AttractionsEntity> attraction);


}

