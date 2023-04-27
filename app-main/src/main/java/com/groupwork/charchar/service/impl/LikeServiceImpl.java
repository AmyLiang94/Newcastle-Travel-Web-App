package com.groupwork.charchar.service.impl;

import com.groupwork.charchar.service.ILikeService;
import com.groupwork.charchar.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * description: about like methods
 */
@Service
public class LikeServiceImpl implements ILikeService {
    private Logger logger = LoggerFactory.getLogger(LikeServiceImpl.class);
    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public void like(int userId, int reviewId) {
        try {
            String likeKey = RedisKeyUtil.getRedisLikeKey(reviewId);
            Boolean isMember = redisTemplate.opsForSet().isMember(likeKey, userId);
            if (Boolean.TRUE.equals(isMember)) {
                redisTemplate.opsForSet().remove(likeKey, userId);
            } else {
                redisTemplate.opsForSet().add(likeKey, userId);
            }
        } catch (Exception e) {
            logger.error("Error occurred while using like method for userId: {} and reviewId: {}", userId, reviewId, e);
        }

    }

    public long findLikeCount (int reviewId) {
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
     * @param userId
     * @param reviewId
     * @return
     */
    public int findCurUserLikeStatus (int userId, int reviewId) {
        try {
            String likeKey = RedisKeyUtil.getRedisLikeKey(reviewId);
            return redisTemplate.opsForSet().isMember(likeKey, userId) ? 1 : 0;
        } catch (Exception e) {
            logger.error("Error occurred while using findingCurUserLike method for userId: {} and reviewId: {}", userId, reviewId, e);
            return 0;
        }
    }

    /**
     * 更新最新的点赞数量评论
     * @param attractionId
     * @param reviewId
     */
    public void updateReviewLikeCount(int attractionId, int reviewId) {
        int likeCount = (int) this.findLikeCount(reviewId);
        String key = RedisKeyUtil.getRedisAttractionReviewKey(attractionId);
        redisTemplate.opsForZSet().add(key, reviewId, likeCount);
    }

    /**
     * 获取点赞数最高的评论
     * @param attractionId
     * @param topN 获取的数量
     * @return
     */

    public Set<Integer> getTopReviews(int attractionId, int topN) {
        String key = RedisKeyUtil.getRedisAttractionReviewKey(attractionId);
        return redisTemplate.opsForZSet().reverseRange(key, 0, topN - 1);
    }


}
