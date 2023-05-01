package com.groupwork.charchar.service;

import java.util.Set;

public interface ILikeService {
    public void like(int userId, int reviewId);
    public long findLikeCount (int reviewId);
    public int findCurUserLikeStatus (int userId, int reviewId);
    public void updateReviewLikeCount(int attractionId, int reviewId);
    public Set<Integer> getTopReviews(int attractionId, int topN);
}
