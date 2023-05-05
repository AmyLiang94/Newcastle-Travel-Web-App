package com.groupwork.charchar.service;

import com.groupwork.charchar.vo.TopReviewsVO;

import java.util.List;

public interface ILikeService {
    public void like(Integer userId, Integer reviewId);
    public long findLikeCount (Integer reviewId);
    public int findCurUserLikeStatus (Integer userId, Integer reviewId);
    public void updateReviewLikeCount(Integer attractionId);
    public List<TopReviewsVO> getTopReviews(Integer attractionId, Integer topN);
}
