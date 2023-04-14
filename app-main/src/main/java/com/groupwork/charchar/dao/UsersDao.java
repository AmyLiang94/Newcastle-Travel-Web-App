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
    //通过userName查找用户所有信息
    @Select("SELECT username,email, password, salt,answer1,answer2,answer3 FROM users where email=#{email}")
    public List<UsersEntity> selectEmail(@Param("email") String email);

    //通过userName查找用户个人信息
    @Select("SELECT username,email, phone, user_location FROM users where email=#{email}")
    public List<UsersEntity> getByUserEmail(@Param("email") String email);

    @Update("UPDATE users SET password=#{password}, salt=#{salt} WHERE email=#{email}")
    void updatePwd(@Param("email") String email, @Param("password") String password, @Param("salt") String salt);

    @Update("UPDATE users SET username=#{username}, phone=#{phone} WHERE email=#{email}")
    void updateUserInformation(@Param("username") String username, @Param("email") String email, @Param("phone") String phone);

    @Insert("INSERT INTO users(username, password,email, salt, phone, user_location, question1, answer1, question2, answer2, question3, answer3) VALUES (#{username}, #{password}, #{email},#{salt}, #{phone}, #{userLocation}, #{question1}, #{answer1},#{question2}, #{answer2}, #{question3}, #{answer3})")
    void save(UsersEntity user);

    @Delete("DELETE FROM users WHERE email=#{email}")
    void deleteUser(@Param("email") String email);



}
