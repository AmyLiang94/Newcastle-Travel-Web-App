package com.groupwork.charchar.controller;

import com.groupwork.charchar.dao.UsersDao;
import com.groupwork.charchar.entity.ReviewsEntity;
import com.groupwork.charchar.entity.UsersEntity;
import com.groupwork.charchar.service.ILikeService;
import com.groupwork.charchar.service.ReviewsService;
import com.groupwork.charchar.vo.ReviewsDetailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("charchar/reviews")
public class ReviewsController {
    private Logger logger = LoggerFactory.getLogger(ReviewsController.class);
    @Autowired
    private ReviewsService reviewsService;
    @Autowired
    private ILikeService likeService;
    @Autowired
    private UsersDao usersDao;
    public final static int IS_VALID = 1;

    /**
     * 获取某个景点的所有评论
     *
     * @param attractionId attraction id
     */
    @GetMapping("/list/attr/{attractionId}/{userId}")
    public List<ReviewsDetailVO> listReviewsByAttraction(@PathVariable("attractionId") Integer attractionId,
                                                         @PathVariable("userId") Integer userId) {
        List<ReviewsEntity> reviews = reviewsService.listReviewsByAttractionId(attractionId);
        List<ReviewsDetailVO> res = new ArrayList<>();
        for (ReviewsEntity review : reviews) {
            UsersEntity usersEntity = usersDao.selectUserEntityById(review.getUserId());
            if (null == usersEntity) {
                throw new RuntimeException("userId:" + review.getUserId() + " not exists.");
            }
            if (usersEntity.getIsValid() != IS_VALID) {
                throw new RuntimeException("userId:" + review.getUserId() +" not valid");
            }
            ReviewsDetailVO reviewsDetailVO = new ReviewsDetailVO();
//            Map<String, Object> likeMap = likeController.like(userId, review.getReviewId());
            reviewsDetailVO.setReviewId(review.getReviewId());
            reviewsDetailVO.setAttractionId(review.getAttractionId());
            reviewsDetailVO.setUserId(review.getUserId());
            reviewsDetailVO.setUsername(usersEntity.getUsername());
            reviewsDetailVO.setRating(review.getRating());
            reviewsDetailVO.setReviewText(review.getReviewText());
            reviewsDetailVO.setCreateTime(review.getCreateTime());
            reviewsDetailVO.setUpdateTime(review.getUpdateTime());
            long likeCount = likeService.findLikeCount(reviewsDetailVO.getReviewId());
            int likeStatus = likeService.findCurUserLikeStatus(userId, reviewsDetailVO.getReviewId());
            reviewsDetailVO.setLikeStatus(likeStatus);
            reviewsDetailVO.setLikeCount(likeCount);
            res.add(reviewsDetailVO);
        }
        return res;
    }

    /**
     * 获取某个用户的所有评论
     *
     * @userId user id
     */
    @GetMapping("/list/user/{userId}")
    public List<ReviewsEntity> listReviewsByUser(@PathVariable("userId") Integer userId) {
        List<ReviewsEntity> reviews = reviewsService.listReviewsByUserId(userId);
        return reviews;
    }


    /**
     * 保存
     */
    @PostMapping("/save")
    public Map<String, Boolean> saveReview(@RequestBody ReviewsEntity reviews) {
        boolean success = reviewsService.save(reviews);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", success);
        return response;
    }


    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public Map<String, Boolean> deleteReview(@RequestBody Integer[] reviewIds) {
        boolean success = reviewsService.removeByIds(Arrays.asList(reviewIds));
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", success);
        return response;
    }

}
