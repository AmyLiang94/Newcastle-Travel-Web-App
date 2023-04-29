package com.groupwork.charchar.controller;

import com.groupwork.charchar.service.ILikeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {
    @Resource
    ILikeService likeService;
    @PostMapping("/like")
    public Map<String, Object> like(int userId, int reviewId) {
        likeService.like(userId, reviewId);
        long likeCount = likeService.findLikeCount(reviewId);
        int likeStatus = likeService.findCurUserLikeStatus(userId, reviewId);
        Map<String, Object> resMap= new HashMap<>();
        resMap.put("likeCount", likeCount);
        resMap.put("likeStatus", likeStatus);
        return resMap;
    }

}
