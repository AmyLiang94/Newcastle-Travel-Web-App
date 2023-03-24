package com.groupwork.charchar.controller;

import java.util.Arrays;
import java.util.Map;

import com.group.charchar.utils.PageUtils;
import com.group.charchar.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = reviewsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{reviewId}")
    public R info(@PathVariable("reviewId") Integer reviewId) {
        ReviewsEntity reviews = reviewsService.getById(reviewId);

        return R.ok().put("reviews", reviews);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ReviewsEntity reviews) {
        reviewsService.save(reviews);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ReviewsEntity reviews) {
        reviewsService.updateById(reviews);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] reviewIds) {
        reviewsService.removeByIds(Arrays.asList(reviewIds));

        return R.ok();
    }

}
