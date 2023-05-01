package com.groupwork.charchar.dao;

import com.groupwork.charchar.entity.UsersEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@Mapper
public interface UsersDao extends BaseMapper<UsersEntity> {
    @Select("SELECT username,email, password, salt FROM users where email=#{email}")
    public List<UsersEntity> selectEmail(@Param("email") String email);

    //通过userName查找用户个人信息
    @Select("SELECT username,email FROM users where email=#{email}")
    public List<UsersEntity> getByUserEmail(@Param("email") String email);

    @Select("SELECT email, activation_time FROM users WHERE confirm_code = #{confirmCode} AND is_valid = 0")
    UsersEntity selectUserByConfirmCode(@Param("confirmCode") String confirmCode);

    @Select("SELECT verification_code FROM users WHERE email=#{email}")
    public List<UsersEntity> findVerifiCode(@Param("email") String email);


    @Update("UPDATE users SET password=#{password}, salt=#{salt} WHERE email=#{email}")
    void updatePwd(@Param("email") String email, @Param("password") String password, @Param("salt") String salt);

    @Update("UPDATE users SET username=#{username} WHERE email=#{email}")
    void updateUserInformation(@Param("username") String username, @Param("email") String email);

    @Update("UPDATE users SET verification_code=#{verificationCode} WHERE email=#{email}")
    void updateVertificationCode(@Param("email") String email,@Param("verificationCode") String verificationCode);

    @Update("UPDATE users SET is_valid = 1 WHERE confirm_code = #{confirmCode}")
    int updateUserByConfirmCode(@Param("confirmCode") String confirmCode);

    @Insert("INSERT INTO users(username, password,email, salt, confirm_code, activation_time, is_valid) VALUES (#{username}, #{password}, #{email}, #{salt}, #{confirmCode}, #{activationTime}, #{isValid})")
    int save(UsersEntity user);

    @Delete("DELETE FROM users WHERE email=#{email}")
    void deleteUser(@Param("email") String email);



}
