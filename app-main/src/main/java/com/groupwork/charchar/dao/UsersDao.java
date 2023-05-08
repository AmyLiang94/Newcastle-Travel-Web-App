package com.groupwork.charchar.dao;

import com.groupwork.charchar.entity.UsersEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Eastman
 * @email 931654949@qq.com
 * @date 2023-05-02 15:33:03
 */
@Mapper
public interface UsersDao extends BaseMapper<UsersEntity> {
    /**
     * interface
     * @param email
     * @return  user_id, username, email, password, salt
     */
    @Select("SELECT  user_id, username, email, password, salt, is_valid FROM users where email=#{email}")
    public List<UsersEntity> selectEmail(@Param("email") String email);
    /**
     * interface
     * @param userId
     * @return  user_id, username, email, is_valid
     */
    @Select("SELECT  user_id, username, email, is_valid FROM users where user_id=#{userId}")
    public UsersEntity selectUserEntityById(@Param("userId") Integer userId);

    /**
     *  interface
     * @param email
     * @return  user_id,username,email
     */
    //通过userName查找用户个人信息
    @Select("SELECT user_id,username,email FROM users where email=#{email}")
    public List<UsersEntity> getByUserEmail(@Param("email") String email);

    /**
     *  interface
     * @param confirmCode
     * @return  email, activation_time
     */
    @Select("SELECT email, activation_time FROM users WHERE confirm_code = #{confirmCode} AND is_valid = 0")
    UsersEntity selectUserByConfirmCode(@Param("confirmCode") String confirmCode);

    /**
     *  interface
     * @param email
     * @return  verification_code
     */
    @Select("SELECT verification_code FROM users WHERE email=#{email}")
    public List<UsersEntity> findVerifiCode(@Param("email") String email);

    /**
     *  interface
     * @param email
     * @param password
     * @param salt
     */
    @Update("UPDATE users SET password=#{password}, salt=#{salt} WHERE email=#{email}")
    void updatePwd(@Param("email") String email, @Param("password") String password, @Param("salt") String salt);

    /**
     *  interface
     * @param username
     * @param email
     */
    @Update("UPDATE users SET username=#{username} WHERE email=#{email}")
    void updateUserInformation(@Param("username") String username, @Param("email") String email);

    /**
     *  interface
     * @param email
     * @param verificationCode
     */
    @Update("UPDATE users SET verification_code=#{verificationCode} WHERE email=#{email}")
    void updateVertificationCode(@Param("email") String email,@Param("verificationCode") String verificationCode);

    /**
     *  interface
     * @param confirmCode
     * @return
     */
    @Update("UPDATE users SET is_valid = 1 WHERE confirm_code = #{confirmCode}")
    int updateUserByConfirmCode(@Param("confirmCode") String confirmCode);

    /**
     *  interface
     * @param user
     * @return
     */
    @Insert("INSERT INTO users(username, password,email, salt, confirm_code, activation_time, is_valid) VALUES (#{username}, #{password}, #{email}, #{salt}, #{confirmCode}, #{activationTime}, #{isValid})")
    int save(UsersEntity user);

    /**
     *  interface
     * @param email
     */
    @Delete("DELETE FROM users WHERE email=#{email}")
    void deleteUser(@Param("email") String email);

}
