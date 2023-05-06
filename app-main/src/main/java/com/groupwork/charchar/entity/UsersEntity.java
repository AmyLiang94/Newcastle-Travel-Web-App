package com.groupwork.charchar.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Eastman
 * @email 931654949@qq.com
 * @date 2023-05-24 15:33:03
 */
@Data
@NoArgsConstructor//有参无参构造器
@AllArgsConstructor
@TableName("users")
public class UsersEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *  Specifying a primary key generation policy
     */
    @TableId(type = IdType.AUTO)
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
    private String salt;
    /**
     *
     */
    private Date createdTime;
    /**
     *
     */
    private Date updatedTime;
    /**
     *  Confirmation Code
     */
    @TableField("confirm_code")
    private String confirmCode;
    /**
     *
     */
    @TableField("activation_time")
    private LocalDateTime activationTime; // 激活失效时间
    /**
     *
     */
    @TableField("is_valid")
    private Byte isValid; // 是否可用
    /**
     *
     */
    @TableField("verification_code")
    private String verificationCode;

}
