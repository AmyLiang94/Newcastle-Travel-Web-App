package com.groupwork.charchar.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @className: AttractionDetailVO
 * @Author: wyl
 * @Description:
 * @Date: 07/05/2023 16:42
 */
@Data
public class AttractionDetailVO {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
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

    private String walkingTime;
}
