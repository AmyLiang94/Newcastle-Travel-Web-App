package com.groupwork.charchar.controller;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.groupwork.charchar.entity.ReviewsEntity;
import com.groupwork.charchar.service.ReviewsService;


/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@RestController
@RequestMapping("product/reviews")
public class ReviewsController {
    @Autowired
    private ReviewsService reviewsService;

    /**
     * 获取某个景点的所有评论
     *
     * @param attractionId attraction id
     */
    @GetMapping("/list/attr/{attractionId}")
    public List<ReviewsEntity> listReviewsByAttraction(@PathVariable("attractionId") Integer attractionId) {
        List<ReviewsEntity> reviews = reviewsService.listReviewsByAttractionId(attractionId);
        return reviews;
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
     * 分页获取某个景点的所有评论
     *
     * @param attractionId attraction id
     */
    @GetMapping("/list/attr/{attractionId}/{page}/{size}")
    public IPage<ReviewsEntity> listReviewsByAttractionWithPage(@PathVariable("attractionId") Integer attractionId, @PathVariable Integer page, @PathVariable Integer size) {
        IPage<ReviewsEntity> reviews = reviewsService.listReviewsByAttractionIdWithPage(attractionId, page, size);
        return reviews;
    }

    /**
     * 分页获取某个用户的所有评论
     *
     * @userId user id
     */
    @GetMapping("/list/user/{userId}/{page}/{size}")
    public IPage<ReviewsEntity> listReviewsByUserWithPage(@PathVariable("userId") Integer userId, @PathVariable Integer page, @PathVariable Integer size) {
        IPage<ReviewsEntity> reviews = reviewsService.listReviewsByUserIdWithPage(userId, page, size);
        return reviews;
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public boolean saveReview(@RequestBody ReviewsEntity reviews) {
        reviewsService.save(reviews);
        return true;
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public boolean updateReview(@RequestBody ReviewsEntity reviews) {
        reviewsService.updateById(reviews);
        return true;
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public boolean deleteReview(@RequestBody Integer[] reviewIds) {
        reviewsService.removeByIds(Arrays.asList(reviewIds));
        return true;
    }

}
