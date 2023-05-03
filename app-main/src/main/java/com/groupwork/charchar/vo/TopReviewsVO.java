package com.groupwork.charchar.vo;

import lombok.Data;

/**
 * @className: LikeVO
 * @Author: wyl
 * @Description:
 * @Date: 03/05/2023 17:28
 */
@Data
public class TopReviewsVO {
    private Integer userId;
    private String reviewContent;
    private Integer rating;
    private Integer likeCount;

}
