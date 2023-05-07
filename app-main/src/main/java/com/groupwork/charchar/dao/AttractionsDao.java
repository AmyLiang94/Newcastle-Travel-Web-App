package com.groupwork.charchar.dao;

import com.groupwork.charchar.entity.AttractionsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@Mapper
public interface AttractionsDao extends BaseMapper<AttractionsEntity> {

    AttractionsEntity getAttractionById(Integer attractionId);

    int updateAttractionRating(Integer attractionId, Double attrRating);

    AttractionsEntity getAttractionByplaceId(String placeId);

    AttractionsEntity findByPlaceId(String placeId);
}
