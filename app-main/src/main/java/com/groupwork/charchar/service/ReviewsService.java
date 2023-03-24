package com.groupwork.charchar.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.group.charchar.utils.PageUtils;
import com.groupwork.charchar.entity.ReviewsEntity;

import java.util.Map;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
public interface ReviewsService extends IService<ReviewsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

