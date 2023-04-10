package com.groupwork.charchar.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.group.charchar.utils.PageUtils;
import com.groupwork.charchar.entity.AttractionsEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
public interface AttractionsService extends IService<AttractionsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<AttractionsEntity> getNearByLocation(double latitude, double longitude, double radius) throws IOException;

}

