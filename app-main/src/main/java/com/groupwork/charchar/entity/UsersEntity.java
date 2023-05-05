package com.groupwork.charchar.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@Data
@NoArgsConstructor//有参无参构造器
@AllArgsConstructor
@TableName("users")
public class UsersEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *ID
     */
    @TableId(type = IdType.AUTO)	//指定主键生成策略
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
     *雪花数，用于加密
     */
    private String salt;
    /**
     *
     */

    private Date createdTime;
    /**
     *
     */

    private Date updatedTime;

    @TableField("confirm_code")
    private String confirmCode; // 确认码

    @TableField("activation_time")
    private LocalDateTime activationTime; // 激活失效时间

    @TableField("is_valid")
    private Byte isValid; // 是否可用

    @TableField("verification_code")
    private String verificationCode;



}
