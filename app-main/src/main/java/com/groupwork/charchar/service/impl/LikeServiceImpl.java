package com.groupwork.charchar.service.impl;

import com.groupwork.charchar.dao.UsersDao;
import com.groupwork.charchar.entity.ReviewsEntity;
import com.groupwork.charchar.service.ILikeService;
import com.groupwork.charchar.service.ReviewsService;
import com.groupwork.charchar.utils.RedisKeyUtil;
import com.groupwork.charchar.vo.TopReviewsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * description: about like methods
 */
@Service
public class LikeServiceImpl implements ILikeService {
    private Logger logger = LoggerFactory.getLogger(LikeServiceImpl.class);
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private UsersDao usersDao;
    @Resource
    private ReviewsService reviewsService;

    @Override
    public void like(Integer userId, Integer reviewId) {
        try {
            String likeKey = RedisKeyUtil.getRedisLikeKey(reviewId);
            Boolean isMember = redisTemplate.opsForSet().isMember(likeKey, userId);
            if (Boolean.TRUE.equals(isMember)) {
                redisTemplate.opsForSet().remove(likeKey, userId);
            } else {
                redisTemplate.opsForSet().add(likeKey, userId);
            }
            // 更新景点对应的点赞数量的评论
            int attractionId = reviewsService.getReviewEntityById(reviewId).getAttractionId(); // 获取景点ID，你可能需要实现这个方法
            String attractionReviewKey = RedisKeyUtil.getRedisAttractionReviewKey(attractionId);
            long newLikeCount = this.findLikeCount(reviewId); // 计算新的点赞数量
            redisTemplate.opsForZSet().add(attractionReviewKey, reviewId, newLikeCount); // 更新ZSet中的点赞数量

        } catch (Exception e) {
            logger.error("Error occurred while using like method for userId: {} and reviewId: {}", userId, reviewId, e);
        }

    }

    public long findLikeCount(Integer reviewId) {
        try {
            String likeKey = RedisKeyUtil.getRedisLikeKey(reviewId);
            return redisTemplate.opsForSet().size(likeKey);
        } catch (Exception e) {
            logger.error("Error occurred while findLikeCount method count for reviewId: {}", reviewId, e);
            return 0L;
        }
    }

    /**
     * 记录用户当前对这个评论是否点过赞，1是点过赞，0是没点赞
     *
     * @param userId
     * @param reviewId
     * @return
     */
    public int findCurUserLikeStatus(Integer userId, Integer reviewId) {
        try {
            String likeKey = RedisKeyUtil.getRedisLikeKey(reviewId);
            return redisTemplate.opsForSet().isMember(likeKey, userId) ? 1 : 0;
        } catch (Exception e) {
            logger.error("Error occurred while using findingCurUserLike method for userId: {} and reviewId: {}", userId, reviewId, e);
            return 0;
        }
    }

    /**
     * 更新景点最新的点赞数量评论
     *
     * @param attractionId
     */
    public void updateReviewLikeCount(Integer attractionId) {
        // 获取与attractionId相关的所有评论
        List<ReviewsEntity> reviewsEntities = reviewsService.getReviewsByAttractionId(attractionId);
        String key = RedisKeyUtil.getRedisAttractionReviewKey(attractionId);

        // 遍历所有评论
        for (ReviewsEntity reviewsEntity : reviewsEntities) {
            int reviewId = reviewsEntity.getReviewId();
            int likeCount = (int) this.findLikeCount(reviewId);
            // 将评论ID（reviewId）和对应的点赞数量（likeCount）添加到Redis的有序集合（ZSet）中
            redisTemplate.opsForZSet().add(key, reviewId, likeCount);
        }
    }

    /**
     * 获取点赞数最高的评论
     *
     * @param attractionId
     * @param topN         获取的数量
     * @return
     */

    public List<TopReviewsVO> getTopReviews(Integer attractionId, Integer topN) {
        String key = RedisKeyUtil.getRedisAttractionReviewKey(attractionId);
        Set<Integer> ids = redisTemplate.opsForZSet().reverseRange(key, 0, topN - 1);
        List<TopReviewsVO> topReviewsInfo = new ArrayList<>();
        for (Integer reviewId : ids) {
            ReviewsEntity reviewEntity = reviewsService.getReviewEntityById(reviewId);
            TopReviewsVO topReviewsVo = new TopReviewsVO();
            topReviewsVo.setUserId(reviewEntity.getUserId());
            topReviewsVo.setReviewContent(reviewEntity.getReviewText());
            topReviewsVo.setRating(reviewEntity.getRating());
            topReviewsVo.setLikeCount((int) this.findLikeCount(reviewId));
            topReviewsInfo.add(topReviewsVo);
        }
        return topReviewsInfo;
    }


}
