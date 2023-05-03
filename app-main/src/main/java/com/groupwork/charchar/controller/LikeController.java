package com.groupwork.charchar.controller;

import com.groupwork.charchar.service.ILikeService;
import com.groupwork.charchar.vo.TopReviewsVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("charchar/like")
public class LikeController {
    @Resource
    ILikeService likeService;
    @PostMapping("/giveLike")
    public Map<String, Object> like(int userId, int reviewId) {
        likeService.like(userId, reviewId);
        long likeCount = likeService.findLikeCount(reviewId);
        int likeStatus = likeService.findCurUserLikeStatus(userId, reviewId);
        Map<String, Object> resMap= new HashMap<>();
        resMap.put("likeCount", likeCount);
        resMap.put("likeStatus", likeStatus);
        return resMap;
    }

    /**
     * 更新景点最新的景点点赞数量
     * @param attractionId
     */
    @PostMapping("/updateReviewLike")
    public void updateReviewLikeCount(int attractionId) {
        likeService.updateReviewLikeCount(attractionId);
    }

    /**
     * 获取一个景点最高点赞的评论（多条）
     * @param attractionId
     * @param topN
     * @return
     */
    @GetMapping("/getTopReviews/{attractionId}/{topN}")
    public List<TopReviewsVO> getTopReviews(@PathVariable("attractionId")int attractionId,
                                            @PathVariable("topN")int topN) {
        return likeService.getTopReviews(attractionId, topN);
    }
}
