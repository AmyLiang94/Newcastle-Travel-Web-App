package com.groupwork.charchar.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@Data
@TableName("attractions")
public class AttractionsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer attractionId;
    /**
     *
     */
    private String attractionName;
    /**
     *
     */
    private String description;
    /**
     *
     */
    private String category;
    /**
     *
     */
    private BigDecimal latitude;
    /**
     *
     */
    private BigDecimal longitude;
    /**
     *
     */
    private String openingHours;
    /**
     *
     */
    private BigDecimal ticketPrice;
    /**
     *
     */
    private String imageUrl;
    /**
     *
     */
    private Double attrRating;
    /**
     *
     */
    private Integer wheelchairAllow;
    /**
     *
     */
    private Integer pramAllow;
    /**
     *
     */
    private Integer hearingAllow;
    /**
     *
     */
    private String address;
    /**
     *
     */
    private String placeId;
}
