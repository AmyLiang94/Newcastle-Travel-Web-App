package com.groupwork.charchar.dao;

import com.groupwork.charchar.entity.UsersEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@Mapper
public interface UsersDao extends BaseMapper<UsersEntity> {
    @Select("SELECT username, password, salt,answer1,answer2,answer3 FROM users where username=#{username}")
    public List<UsersEntity> selectUserName(@Param("username") String username);

    @Update("UPDATE users SET password=#{password}, salt=#{salt} WHERE username=#{username}")
    void updatePwd(@Param("username") String username, @Param("password") String password, @Param("salt") String salt);

    @Insert("INSERT INTO users(username, password,email, salt, phone, user_location, question1, answer1, question2, answer2, question3, answer3) VALUES (#{username}, #{password}, #{email},#{salt}, #{phone}, #{userLocation}, #{question1}, #{answer1},#{question2}, #{answer2}, #{question3}, #{answer3})")
    void save(UsersEntity user);


}
