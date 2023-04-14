package com.groupwork.charchar.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
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
    private String phone;
    /**
     *
     */
    @TableField("user_location")
    private String userLocation;
    /**
     *
     */

    private Date createdTime;
    /**
     *
     */

    private Date updatedTime;
    /**
     *忘记密码功能的三个问题
     */
//    @TableField("question_1")
    private String question1;
    /**
     *
     */
//    @TableField("answer_1")
    private String answer1;
    /**
     *
     */
//    @TableField("question_2")
    private String question2;
    /**
     *
     */
//    @TableField("answer_2")
    private String answer2;
    /**
     *
     */
//    @TableField("question_3")
    private String question3;
    /**
     *
     */
//    @TableField("answer_3")
    private String answer3;



}
