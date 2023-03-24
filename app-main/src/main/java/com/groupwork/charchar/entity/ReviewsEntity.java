package com.groupwork.charchar.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@Data
@TableName("reviews")
public class ReviewsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

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
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date updateTime;

}
