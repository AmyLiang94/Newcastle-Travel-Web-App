package com.groupwork.charchar.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @className: reviewsDetailVO
 * @Author: wyl
 * @Description:
 * @Date: 07/05/2023 16:35
 */
@Data
public class ReviewsDetailVO {
    /**
     *
     */
    @TableId
    private Integer reviewId;
    /**
     *
     */
    private Integer attractionId;
    /**
     *
     */
    private Integer userId;
    /**
     *
     */
    private Integer rating;
    /**
     *
     */
    private String reviewText;

    private Long likeCount;

    /**
     * 0 未点赞， 1点过赞
     */
    private Integer likeStatus;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date updateTime;


}
