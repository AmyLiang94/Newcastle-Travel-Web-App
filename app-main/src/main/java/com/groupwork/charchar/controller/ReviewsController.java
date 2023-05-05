package com.groupwork.charchar.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.groupwork.charchar.entity.ReviewsEntity;
import com.groupwork.charchar.service.ReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("charchar/reviews")
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
    public Map<String, Boolean> saveReview(@RequestBody ReviewsEntity reviews) {
        boolean success = reviewsService.save(reviews);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", success);
        return response;
    }

//    /**
//     * 修改
//     */
//    @PutMapping("/update")
//    public Map<String, Boolean> updateReview(@RequestBody ReviewsEntity reviews) {
//        boolean success = reviewsService.updateById(reviews);
//        Map<String, Boolean> response = new HashMap<>();
//        response.put("success", success);
//        return response;
//    }

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
