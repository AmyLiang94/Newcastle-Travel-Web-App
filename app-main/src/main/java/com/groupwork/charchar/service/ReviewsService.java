package com.groupwork.charchar.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.groupwork.charchar.entity.ReviewsEntity;

import java.util.List;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
public interface ReviewsService extends IService<ReviewsEntity> {


    List<ReviewsEntity> listReviewsByAttractionId(Integer attractionId);

    List<ReviewsEntity> listReviewsByUserId(Integer userId);

    IPage<ReviewsEntity> listReviewsByAttractionIdWithPage(Integer attractionId, Integer page, Integer size);

    IPage<ReviewsEntity> listReviewsByUserIdWithPage(Integer userId, Integer page, Integer size);

    ReviewsEntity getReviewEntityById(int reviewId);

    List<ReviewsEntity> getReviewsByAttractionId(int attractionId);
}

