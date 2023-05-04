package com.groupwork.charchar.service;

import com.groupwork.charchar.vo.TopReviewsVO;

import java.util.List;

public interface ILikeService {
    public void like(int userId, int reviewId);
    public long findLikeCount (int reviewId);
    public int findCurUserLikeStatus (int userId, int reviewId);
    public void updateReviewLikeCount(int attractionId);
    public List<TopReviewsVO> getTopReviews(int attractionId, int topN);
}
