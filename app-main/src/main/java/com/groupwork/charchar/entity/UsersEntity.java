package com.groupwork.charchar.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@Data
@TableName("users")
public class UsersEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId()
    private Integer userId;
    /**
     *
     */
    private String username;
    /**
     *
     */
    private String email;
    /**
     *
     */
    private String password;
    /**
     *
     */
    private String phone;
    /**
     *
     */
    private String userLocation;
    /**
     *
     */

    private Date createdTime;
    /**
     *
     */

    private Date updatedTime;

}
